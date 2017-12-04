

package com.demo.si.file.process.utils;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author asv132 - Karthik Palanivelu on 10/16/17.
 */
public class CustomToStringStyle extends ToStringStyle {

    public static final ToStringStyle NO_CLASS_NAME_MULTI_LINE_STYLE = new CustomToStringStyle.NoClassNameMultiLineToStringStyle();

    private static final class NoClassNameMultiLineToStringStyle extends ToStringStyle {

        private static final long serialVersionUID = 1L;

        /**
         * <p>Constructor.</p>
         * <p>
         * <p>Use the static constant rather than instantiating.</p>
         */
        NoClassNameMultiLineToStringStyle() {
            super();
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
            this.setContentStart("\n");
            this.setFieldSeparator(System.lineSeparator() + "  ");
            this.setFieldSeparatorAtStart(true);
            this.setContentEnd(System.lineSeparator());
        }

        /**
         * <p>Ensure <code>Singleton</code> after serialization.</p>
         *
         * @return the singleton
         */
        private Object readResolve() {
            return CustomToStringStyle.NO_CLASS_NAME_MULTI_LINE_STYLE;
        }

    }

}
