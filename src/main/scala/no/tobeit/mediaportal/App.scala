package no.tobeit.mediaportal

/**
 * Hello world!
 *
 */
import java.io.File

object App extends Application {
  val files = Set("a.mp3", "b.wmv", "c.vob", "tdir/d.avi", "tdir/e.exe", "tdir/.f.ogg")
  val filetypes = Set("mp3", "wmv", "avi", "ogg")
  val basedir = "/tmp"
  
  for (filename <- files) {
    val file = new File(basedir, filename)
    file.deleteOnExit
  }

  val matches = FileSearcher.findFilesOfType(basedir, filetypes)
  for (m <- matches) Console.println(m.getAbsoluteFile + m.getAbsolutePath)
}
