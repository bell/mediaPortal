package no.tobeit.mediaportal

import no.tobeit.mediaportal.utils.RichFile._ // makes implicit toRichFile active
import java.io.File


object FileSearcher {

  def findFilesOfType(baseDir:String, validTypes: Set[String]): Iterable[File] = {
    val validSet = validTypes
    if (validTypes.isEmpty) {
      return Iterable.empty
    }
    if (!validSet.isEmpty) {
      return for (f <- new File(baseDir).andTree; if endsWith(f.getName, validTypes))
             yield f
    }
    Iterable.empty
  }

  def endsWith(name:String, validTypes:Set[String]):Boolean = {
    if (name.contains(".")) {
      val indexOfDot = name.lastIndexOf(".") + 1;
      if (validTypes.contains(name.substring(indexOfDot))) {
        return true
      }
    }
    false
  }
  
}