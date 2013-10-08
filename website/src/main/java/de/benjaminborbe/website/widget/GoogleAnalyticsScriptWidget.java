package de.benjaminborbe.website.widget;

import java.io.StringWriter;

import de.benjaminborbe.website.util.JavascriptWidget;

public class GoogleAnalyticsScriptWidget extends JavascriptWidget {

    public GoogleAnalyticsScriptWidget() {
        super(buildContent());
    }

    private static String buildContent() {
        final StringWriter sw = new StringWriter();
        sw.append("var _gaq = _gaq || [];\n");
        sw.append("_gaq.push(['_setAccount', 'UA-44692710-1']);\n");
        sw.append("_gaq.push(['_trackPageview']);\n");
        sw.append("(function() {\n");
        sw.append("var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n");
        sw.append("ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n");
        sw.append("var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n");
        sw.append("})();\n");
        return sw.toString();
    }

}
