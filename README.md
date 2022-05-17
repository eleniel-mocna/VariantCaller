# Variant Caller

This is a Java version of my original
[Custom Variant Caller](https://github.com/eleniel-mocna/CustomVariantCaller)
written in C++. Java should provide much better readability and more options
for multi-threading without compromising the speed too much.

## Instalation and usage

This application can be compiled with java 17 and then run in the following way:
```(bash)
<input.sam java -jar VariantCaller.jar [options...]
 --reference VAL : Path to the reference file. (default: /reference/hg19.fa)
 --minBaseQ N    : Minimal base quality. (default: 0)
 --minMapQ N     : Minimal mapping quality. (default: 0)
 --threads N     : Maximum number of processing threads (default: 1)
 --tsv VAL       : Path to the output tsv file. (default: stdout)
```
For example to run this on the sample data run:

`<sample_data/example.sam java -jar VariantCaller.jar --reference sample_data/ref.fa`
## Todos

- Document yet undocumented parts
- Think about unit tests
- Test on bigger inputs (and bigger references)
- Implement multithreading
- Measure speed increase