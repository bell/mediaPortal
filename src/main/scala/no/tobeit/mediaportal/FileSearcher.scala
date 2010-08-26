package no.tobeit.mediaportal

import no.tobeit.mediaportal.utils.RichFile._ // makes implicit toRichFile active
import java.io.File


object FileSearcher {

  def findFilesOfType(baseDir:String, validTypes: Set[String]): Iterable[File] = {
    val validSet = validTypes
    if (validTypes.isEmpty) {
      println("validTypes er tomme... " + validTypes)
      return Iterable.empty
    }
    for (f <- new File(baseDir).andTree;
         if !validSet.isEmpty && f.getName.contains(".") && validSet.contains(f.getName.substring(f.getName.lastIndexOf("."))))
           yield f
  }
  
}