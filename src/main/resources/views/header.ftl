<#macro header title, css, includeCss="">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
    <title>${title}</title>
    <#list css as cssLink>
        <link href="${cssLink}" rel="stylesheet">
    </#list>

    <#if includeCss?has_content >
        <#include includeCss />
    </#if>
</head>
</#macro>