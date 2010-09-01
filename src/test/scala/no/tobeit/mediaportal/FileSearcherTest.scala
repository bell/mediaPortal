package no.tobeit.mediaportal

import org.scalatest.junit.AssertionsForJUnit
import java.io.File
import org.junit.{Test, Before}
import org.junit.Assert.{assertEquals, assertTrue}

class FileSearcherTest extends AssertionsForJUnit {
  val tdir = "tdir"
  val matches = Set("a.mp3", "b.wmv", "c.vob", tdir + "/d.avi", tdir + "/.f.ogg")
  val misses = Set(tdir + "/e.exe", "jalla.bat", ".dings", "dings.")
  val filetypes = Set("mp3", "wmv", "avi", "ogg", "vob")
  val basedir = "/tmp"
  
  @Before def initialize() {
    val td = new File(basedir, tdir)
    td.mkdir
    td.deleteOnExit
    for (filename <- matches) {
      createEmptyFile(basedir, filename)
    }
    for (filename <- misses) {
      createEmptyFile(basedir, filename)
    }
  }

  def createEmptyFile(basedir:String, name:String):Unit = {
    val file = new File(basedir, name)
    if (file.createNewFile) {
      file.deleteOnExit
    }
  }

  @Test def shouldFindValidFilenames() {
    val found = FileSearcher.findFilesOfType(basedir, filetypes)
    assertEquals(matches.size, found.size)
  }
}