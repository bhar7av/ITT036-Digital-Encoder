
# ITT 036 — Digital Signal Encoder and Decoder

A Java implementation of digital line coding, decoding, digital data
generation, palindrome detection, and Pulse Code Modulation (PCM).

## Features

- Random binary data generation
- Generation of data containing four or eight consecutive zeros
- Manual binary input
- Longest palindromic substring detection
- NRZ-L and NRZ-I encoding and decoding
- Manchester and Differential Manchester encoding and decoding
- AMI, B8ZS, and HDB3 encoding and decoding
- Analog sine and cosine sample generation
- PCM encoding, decoding, and quantization
- Signal waveform visualization using Java Swing and AWT
- Automated round-trip and edge-case tests

## Technologies

- Java
- IntelliJ IDEA
- Java Swing and AWT
- Git and GitHub

## How to Run

1. Install a compatible JDK.
2. Clone this repository.
3. Open the project in IntelliJ IDEA.
4. Run `Main.java`.
5. Choose digital line coding or analog signal and PCM.
6. Follow the console instructions.

## Testing

The initial encoder-decoder test suite passed 49 tests.
The edge-case suite passed 77 tests after fixing the B8ZS
decoder's handling of leading zero runs.

These results apply to the tested inputs.

## Screenshots

Add application and waveform screenshots in the `screenshots/`
directory. Embed them here after adding the image files.

## Acknowledgment

OpenAI ChatGPT was used for implementation guidance, debugging,
and test development. The implementation was run and tested by
the student.

## Author

Bhargav Koushal

## Course

ITT 036 — Programming Assignment 1, Autumn 2026
