# Israeli ID Validator and Wordle (Java Console)

> A homework repo that grew up — refactored for algorithmic correctness and code organization.

This repo started as a single-file Java homework assignment containing two unrelated console programs: an Israeli national ID validator (Luhn-variant checksum) and a Wordle clone. Revisiting it surfaced two real correctness bugs — Wordle silently accepted guesses outside its dictionary, and miscounted repeated letters in its yellow feedback — plus a too-permissive validator that called the empty string a valid ID, and the structural smell of packaging two unrelated programs in one `Main.java`. The commit history walks through the revisit: each issue surfaced, fixed, and explained in its own commit.

---

## 🆔 Part 1 — Israeli ID Validator

### What it does

Reads input lines from stdin in a loop. For each line, validates that it's a 1-to-9-digit numeric string, left-pads to 9 digits if shorter, and runs the Israeli national ID checksum. Prints `ID is valid.` or `ID is not valid.`. Exits on `quit` or closed stdin.

### 🏗️ The algorithm — Luhn variant

The Israeli national ID is 9 digits, with the last digit serving as a checksum. Validation walks the digits left-to-right with alternating weights of 1 and 2:

```
position:  0  1  2  3  4  5  6  7  8
weight:    1  2  1  2  1  2  1  2  1
```

For each digit, multiply by its weight. If the weighted result exceeds 9 (only possible when the weight is 2 and the digit is ≥ 5), collapse it by subtracting 9 — equivalent to summing the two decimal digits of the result (`2×7 = 14 → 1+4 = 5 = 14−9`). Sum the collapsed values. The ID is valid if and only if the total is divisible by 10.

This is a Luhn variant — the same family as credit-card checksums. The weight-2 pass and the digit-collapse trick are designed to catch the two error patterns people actually make at a keyboard: single-digit typos (caught completely, since any single-digit change shifts the sum by a nonzero amount mod 10) and adjacent-digit transpositions (caught for nearly every pair — the algorithm has one well-known blind spot, swapping a 0 with a 9, because those two digits happen to produce identical contributions under the weight-2 collapse). It deliberately doesn't catch non-adjacent transpositions or coincidentally compensating substitutions — those are rare in manual entry.

### Edge cases handled

- Empty input → `Please enter an ID.`
- Non-digit input → `ID must contain only digits.`
- More than 9 digits → `ID must be at most 9 digits.`
- Fewer than 9 digits → left-padded with zeros, then validated
- `quit` command → silent exit
- Closed stdin (EOF) → silent exit

---

## 🟩 Part 2 — Wordle Console Game

### What it does

Picks a random 5-letter word from a hardcoded 10-word dictionary. Gives the player six attempts. After each guess, prints per-position feedback: `G` for a position-exact match, `Y` for a letter present elsewhere in the secret, `_` for absent. Rejects guesses not in the dictionary without consuming an attempt. Exits cleanly on EOF.

### 🧠 The repeated-letter problem

The most common bug in DIY Wordle clones is the repeated-letter trap: when a guess contains a letter twice but the secret contains it only once, the naive yellow-marking logic flags both positions yellow. The official NYT Wordle doesn't — it consumes each secret letter at most once.

The original implementation here was the naive version:

```java
if (guess.charAt(i) == secretWord.charAt(i)) {
    feedback[i] = 'G';
} else if (secretWord.contains(String.valueOf(guess.charAt(i)))) {
    feedback[i] = 'Y';
}
```

A single-pass `contains()` check re-finds the same letter for every guess position that has it. With `secret = "apple"` and `guess = "guava"`:

```
old output: __Y_Y    // 'apple' has only one 'a', but both guava 'a's got marked yellow
new output: __Y__    // second guava-'a' correctly underscore — pool already consumed
```

The fix is a two-pass algorithm. Pass 1 walks the positions and marks each exact match as green, removing the matched letter from a mutable copy of the secret's character pool. Pass 2 walks the remaining positions; if the guess letter is still present in the pool, mark yellow and consume one occurrence. Everything else stays underscore.

```java
// Pass 1: greens. Consume matched secret letters from the pool.
for (int i = 0; i < guess.length(); i++) {
    if (guess.charAt(i) == pool[i]) {
        result[i] = 'G';
        pool[i]   = ' ';   // consumed sentinel — no real word has spaces
    }
}

// Pass 2: yellows for non-green positions, consuming from the pool.
for (int i = 0; i < guess.length(); i++) {
    if (result[i] == 'G') continue;
    char c = guess.charAt(i);
    result[i] = '_';
    for (int j = 0; j < pool.length; j++) {
        if (pool[j] == c) {
            result[i] = 'Y';
            pool[j]   = ' ';
            break;
        }
    }
}
```

The single-pass version is the closest analogue in this repo to a subtle correctness gap that quietly ships — most randomly-chosen secret/guess pairs don't have repeated letters, so the bug doesn't fire on typical play. It only surfaces in the specific overlap patterns the naive code can't see.

### Edge cases handled

- Wrong-length guess → `The guess is not a 5 letter word, please revisit your guess and try again.`
- Guess not in dictionary → rejected, attempt not consumed
- Closed stdin (EOF) → silent exit

---

## 🛠️ How to Run

Requires JDK 17+.

```bash
git clone https://github.com/alonbaron/IDChecker_WordleGame.git
cd IDChecker_WordleGame/src
javac *.java

# Run the ID validator
java IsraeliIDValidator

# Run Wordle
java WordleGame
```

Both programs read from stdin and exit on `quit` (ID validator only) or closed stdin (EOF).

---

## 📚 Concepts Touched

- The Luhn algorithm and its national-ID variants
- Input validation patterns: bounds, charset, sentinel commands, EOF
- The repeated-letter trap in naive string-matching algorithms
- Multi-pass algorithms with consume-on-match semantics
- Class extraction and single-responsibility refactoring
- Conventional Commits discipline applied to a revisit-and-fix arc

---

## 📝 License

Educational project — no license. Fork freely for your own learning.
