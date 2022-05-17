/**
 * This is a module for a Variant Caller with a special for paired read variant calling.
 *
 * Now this is only an application not prepared for expansion. It's main structure is as follows:
 * - Reference object provides access to the reference bases.
 * - ReadsProvider object provides Read[] objects via the getNext() method
 * - Core gets the Read objects from ReadProvider and analyzes them compared to the Reference object
 * - Core sends found variants to the VariantsManager
 * - VariantsManager gathers the Variants and produces them via the output() method.
 * - ReportsWriter gets an Iterable of Variants from the VariantsManager and outputs the output string via the
 *      writeVarints() method.
 */
@SuppressWarnings("JavaModuleNaming")
module cz.cuni.mff.soukups3.VariantCaller {
    opens cz.cuni.mff.soukups3.VariantCaller to args4j;
    requires args4j;
}