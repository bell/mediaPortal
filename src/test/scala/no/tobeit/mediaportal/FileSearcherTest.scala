package no.tobeit.mediaportal

import org.scalatest.junit.AssertionsForJUnit
import java.io.File
import org.junit.{Test, Before}

class FileSearcherTest extends AssertionsForJUnit {
  val tdir = "tdir"
  val files = Set("a.mp3", "b.wmv", "c.vob", tdir + "/d.avi", tdir + "/e.exe", tdir + "/.f.ogg")
  val filetypes = Set("mp3", "wmv", "avi", "ogg")
  val basedir = "/tmp"
  
  @Before def initialize() {
    println("starter initialize")
    val td = new File(basedir, tdir)
    td.mkdir
    td.deleteOnExit
    for (filename <- files) {
      val file = new File(basedir, filename)
      if (file.createNewFile) {
        println("Nettopp opprettet fil: " + file)
        file.deleteOnExit
      }

    }
  }

  @Test def shouldFindValidFilenames() {
    val matches = FileSearcher.findFilesOfType(basedir, filetypes)
    for (m <- matches) Console.println(m.getAbsoluteFile + m.getAbsolutePath)
    
  }
}