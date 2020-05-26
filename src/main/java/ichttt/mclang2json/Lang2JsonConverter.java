@@ -107,17 +107,17 @@ public class Lang2JsonConverter {
        BufferedReader reader = null;
        JsonWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); // BufferedReader <- InputStream <- file
            writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8)); // JsonWriter -> OutputStream -> outputfile
            writer.beginObject();
            String BASE_INTENT = LangConverterGui.getIntent();
            String BASE_INTENT = LangConverterGui.getIntent(); // options are: ["2x space", "4x space", "1x tab"]; Gets intent option from JComboBox.
            writer.setIndent(BASE_INTENT);
            boolean extraIndent = false;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("=", 2);

                if (line.trim().isEmpty()) {
                if (line.trim().isEmpty()) { // trim whitespace from front and back of String
                    writer.setIndent("\n" + BASE_INTENT);
                    extraIndent = true;
                    continue;
@ -125,12 +125,12 @@ public class Lang2JsonConverter {

                if (line.charAt(0) == '\uFEFF') {
                    System.out.println("Found BOM encoding - Removing BOM");
                    line = line.substring(1);
                    line = line.substring(1); // line = everything from the 2nd char on. (first char is 1 in comment)
                }
                if (line.trim().charAt(0) == '#') {
                if (line.trim().charAt(0) == '#') { // If line is a comment
                    if (keepComments) {
                        System.out.println("Remapping comment line " + line);
                        writer.name("_comment").value(line.substring(line.indexOf('#') + 1).trim());
                        writer.name("_comment").value(line.substring(line.indexOf('#') + 1).trim()); // value = everything after '#', minus extra whitespace
                        if (extraIndent) {
                            extraIndent = false;
                            writer.setIndent(BASE_INTENT);
@ -144,17 +144,17 @@ public class Lang2JsonConverter {
                if (split.length != 2)
                    throw new RuntimeException("Invalid line " + line + ", it got split into " + split.length);

                String key = remapKey(split[0], modid);
                if (key == null)
                    key = split[0];
                else
                    System.out.println("Remapping key " + split[0] + " to " + key);

                writer.name(key).value(fixupNewline(split[1]));
                if (extraIndent) {
                    extraIndent = false;
                    writer.setIndent(BASE_INTENT);
                }
                // String key = remapKey(split[0], modid);
                // if (key == null)
                //     key = split[0];
                // else
                //     System.out.println("Remapping key " + split[0] + " to " + key);
                //
                // writer.name(key).value(fixupNewline(split[1]));
                // if (extraIndent) {
                //     extraIndent = false;
                //     writer.setIndent(BASE_INTENT);
                // }
            }
            writer.endObject();
            writer.flush();
@ -164,28 +164,28 @@ public class Lang2JsonConverter {
        }
    }

    private static String fixupNewline(String s) {
        return s.replace("\\n", "\n");
    }

    private static String remapKey(String key, String modId) {
        if (key.startsWith("item.") && key.endsWith(".name")) {
      		StringBuilder buf = new StringBuilder("item.");
      		if (modId != null && !modId.isEmpty()) {
      			buf.append(modId).append(".");
      		}
      		buf.append(key, "item.".length(), key.length() - ".name".length()); //very basic remapping
          return buf.toString();
        } else if (key.startsWith("tile.") && key.endsWith(".name")) {
      		StringBuilder buf = new StringBuilder("block.");
       		if (modId != null && !modId.isEmpty()) {
      			buf.append(modId).append(".");
      		}
       		buf.append(key, "tile.".length(), key.length() - ".name".length());
       		return buf.toString();
        }
        return null;
    }
    // private static String fixupNewline(String s) {
    //     return s.replace("\\n", "\n");
    // }

    // private static String remapKey(String key, String modId) {
    //     if (key.startsWith("item.") && key.endsWith(".name")) {
    //   		StringBuilder buf = new StringBuilder("item.");
    //   		if (modId != null && !modId.isEmpty()) {
    //   			buf.append(modId).append(".");
    //   		}
    //   		buf.append(key, "item.".length(), key.length() - ".name".length()); //very basic remapping
    //       return buf.toString();
    //     } else if (key.startsWith("tile.") && key.endsWith(".name")) {
    //   		StringBuilder buf = new StringBuilder("block.");
    //    		if (modId != null && !modId.isEmpty()) {
    //   			buf.append(modId).append(".");
    //   		}
    //    		buf.append(key, "tile.".length(), key.length() - ".name".length());
    //    		return buf.toString();
    //     }
    //     return null;
    // }

    private static void tryClose(Closeable closeable) {
        if (closeable != null) {
