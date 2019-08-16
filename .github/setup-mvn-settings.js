const settingsContent = `<settings>
   <servers>
     <server>
       <id>dev-repo</id>
       <username>maven</username>
       <password>${process.env.MVN_PASS}</password>
     </server>
     <server>
       <id>dev-repo-snap</id>
       <username>maven</username>
       <password>${process.env.MVN_PASS}</password>
     </server>
   </servers>
 </settings>`

require("fs").writeFileSync("./settings.xml", settingsContent)
