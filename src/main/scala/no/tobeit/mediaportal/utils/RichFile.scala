package no.tobeit.mediaportal.utils

import java.io.File

// "borrowed" from http://rosettacode.org/wiki/Walk_a_directory/Recursively#Scala
class RichFile(file: File) {
  
  def children = new Iterable[File] {
    @Override
    def iterator =
      if (file != null && file.isDirectory) {
        val files = file.listFiles
        if (files != null) files.iterator else Iterator.empty
      } else {
        Iterator.empty
      }
  }
  
  def andTree : Iterable[File] = (
    Seq(file) ++ children.flatMap(child => new RichFile(child).andTree)
  )
}

/** implicitely enrich java.io.File with methods of RichFile */
object RichFile {
  implicit def toRichFile(file: File) = new RichFile(file)
}