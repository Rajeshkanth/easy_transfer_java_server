<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${title}</title>
    <link
      rel="shortcut icon"
      href="./assets/images/favicon.ico"
      type="image/x-icon"
    />

    <#list linkTags as tag>
    <link href="${tag.href}" rel="${tag.rel}" />
    </#list>

    <#if scriptTags??>
    <#list scriptTags as tag>
    <script src="${tag.src}"></script>
    </#list>
    </#if>
  </head>
</html>
