package com.trifork.sdm.importer.importers.sks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class SksImporterTest {
    public static File SHAKCompleate = new File("./src/test/resources/testdata/sks/SHAKCOMPLETE.TXT");
    public static File wrong = new File("./src/test/resources/testdata/sks/SHAKCOMPLETE.XML");

    @Test
    public void testAreRequiredInputFilesPresent() {
        SksImporter importer = new SksImporter();
        ArrayList<File> files = new ArrayList<File>();
        assertFalse(importer.areRequiredInputFilesPresent(files));
        files.add(SHAKCompleate);
        assertTrue(importer.areRequiredInputFilesPresent(files));
        files.remove(SHAKCompleate);
        files.add(wrong);
        assertFalse(importer.areRequiredInputFilesPresent(files));
    }


}
