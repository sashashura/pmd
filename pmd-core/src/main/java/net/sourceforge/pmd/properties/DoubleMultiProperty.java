/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.properties;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pmd.properties.builders.MultiNumericPropertyBuilder;
import net.sourceforge.pmd.properties.builders.PropertyBuilderConversionWrapper;


/**
 * Multi-valued double property.
 *
 * @author Brian Remedios
 * @version Refactored June 2017 (6.0.0)
 */
public final class DoubleMultiProperty extends AbstractMultiNumericProperty<Double> {

    /**
     * Constructor using an array of defaults.
     *
     * @param theName        Name
     * @param theDescription Description
     * @param min            Minimum value of the property
     * @param max            Maximum value of the property
     * @param defaultValues  Array of defaults
     * @param theUIOrder     UI order
     * @throws IllegalArgumentException if min > max or one of the defaults is not between the bounds
     */
    public DoubleMultiProperty(String theName, String theDescription, Double min, Double max,
                               Double[] defaultValues, float theUIOrder) {
        this(theName, theDescription, min, max, Arrays.asList(defaultValues), theUIOrder, false);
    }


    /** Master constructor. */
    private DoubleMultiProperty(String theName, String theDescription, Double min, Double max,
                                List<Double> defaultValues, float theUIOrder, boolean isDefinedExternally) {
        super(theName, theDescription, min, max, defaultValues, theUIOrder, isDefinedExternally);
    }


    /**
     * Constructor using a list of defaults.
     *
     * @param theName        Name
     * @param theDescription Description
     * @param min            Minimum value of the property
     * @param max            Maximum value of the property
     * @param defaultValues  List of defaults
     * @param theUIOrder     UI order
     * @throws IllegalArgumentException if min > max or one of the defaults is not between the bounds
     */
    public DoubleMultiProperty(String theName, String theDescription, Double min, Double max,
                               List<Double> defaultValues, float theUIOrder) {
        this(theName, theDescription, min, max, defaultValues, theUIOrder, false);
    }


    @Override
    public Class<Double> type() {
        return Double.class;
    }


    @Override
    protected Double createFrom(String value) {
        return Double.valueOf(value);
    }


    static PropertyBuilderConversionWrapper.MultiValue.Numeric<Double, DoubleMultiPBuilder> extractor() {
        return new PropertyBuilderConversionWrapper.MultiValue.Numeric<Double, DoubleMultiPBuilder>(ValueParserConstants.DOUBLE_PARSER) {
            @Override
            protected DoubleMultiPBuilder newBuilder() {
                return new DoubleMultiPBuilder();
            }
        };
    }


    public static DoubleMultiPBuilder builder(String name) {
        return new DoubleMultiPBuilder().name(name);
    }


    private static class DoubleMultiPBuilder extends MultiNumericPropertyBuilder<Double, DoubleMultiPBuilder> {

        @Override
        protected PropertyDescriptor<List<Double>> createInstance() {
            return new DoubleMultiProperty(name, description, lowerLimit, upperLimit, defaultValues, uiOrder, isDefinedInXML);
        }
    }

}
