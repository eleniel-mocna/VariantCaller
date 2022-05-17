/**
 * This is a module for a Variant Caller with a special for paired read variant calling.
 *
 * Now this is only an application not prepared for expansion. It's main structure is as follows:
 * <ul>
 * <li> Reference object provides access to the reference bases. </li>
 * <li> ReadsProvider object provides Read[] objects via the getNext() method </li>
 * <li> Core gets the Read objects from ReadProvider and analyzes them compared to the Reference object </li>
 * <li> Core sends found variants to the VariantsManager </li>
 * <li> VariantsManager gathers the Variants and produces them via the output() method. </li>
 * <li> ReportsWriter gets an Iterable of Variants from the VariantsManager and outputs the output string via the
 *      writeVariants() method. </li>
 * </ul>
 * For more information see the documentation for each class.
 */
@SuppressWarnings("JavaModuleNaming")
module cz.cuni.mff.soukups3.VariantCaller {
    opens cz.cuni.mff.soukups3.VariantCaller to args4j;
    requires args4j;
}