# Variant Caller

This is a Java version of my original
[Custom Variant Caller](https://github.com/eleniel-mocna/CustomVariantCaller)
written in C++. Java should provide much better readability and more options
for multi-threading without compromising the speed too much.

## Installation and usage

This application can be compiled with java 17 using the pom.xml maven file,
or use the `out/artifacts/VariantCaller_jar/VariantCaller.jar` archive
and then run in the following way:
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

## Short functionality description

For more information see the generated java docs in the `src/resources` folder.

In the core of this utility is a variant caller, which works takes following steps:

- Read given sam file line by line,
- Filter out all reads that:
    - have empty CIGAR string or,
    - have flag 2048 (supplementary alignment) set;
- If two following lines have the same QNAME handle them as pairs, else handle them as reads without a pair,
- Find variants in every pair/read and increment counters for every position,
- If mapQ of a read is lower than filter level, for this read, increment only counters that don't pass quality checks,
- If baseQ for a base in a read is lower than filter level, for this position in this read, increment only counters that don't pass quality checks,
- Print out all found variants

## Output abbreviations meanings

- ADFP (**A**lternate alleles **D**epth on **F**orward strand, **P**air spans this position):
  Depth of variant-supporting bases on forward strand (reads2plus), where the pair read spans this position and doesn't report it.
- ADFU (**A**lternate alleles **D**epth on **F**orward strand, pair does **N**ot span this position):
  Depth of variant-supporting bases on forward strand (reads2plus), where the pair read does not span this position.
- ADRP (**A**lternate alleles **D**epth on **R**everse strand, **P**air spans this position):
  Depth of variant-supporting bases on reverse strand (reads2minus), where the pair read spans this position and doesn't report it.
- ADRU (**A**lternate alleles **D**epth on **R**everse strand, pair does **N**ot span this position):
  Depth of variant-supporting bases on reverse strand (reads2minus), where the pair read does not span this position.
- ADP: Depth of variant-supporting bases on both reads in the pair.
- DP: Total depth
