<% if (!overrides && !implementof) { %>
/**
<% if (isBoolean) { %>
 * Sets whether or not this instance is ${property}.
<% } else { %>
 * Sets the ${property} for this instance.
<% } %>
 *
<% if (array) { %>
 * @param index The index to set.
<% } %>
 * @param ${property} The ${property}.
 */
<% } else if (org_eclipse_jdt_core_compiler_source < "1.5" || (org_eclipse_jdt_core_compiler_source < "1.6" && implementof)) { %>
/**
 * {@inheritDoc}
 * @see ${superType}#${methodSignature}
 */
<% } %>
<% if ((org_eclipse_jdt_core_compiler_source >= "1.5" && overrides) || (org_eclipse_jdt_core_compiler_source >= "1.6" && implementof)) { %>
@Override
<% } %>
public void ${name}(<% if(array) { %>int index, <% } %>${propertyType} ${property})<% if(isinterface) { %>;<% } %>

<% if(!isinterface) { %>
{
<% if(array) { %>
	this.${property}[index] = $property;
<% } else { %>
	this.${property} = ${property};
<% } %>
}
<% } %>
