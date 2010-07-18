package no.tobeit.mediaportal.utils

import java.io.File

// "borrowed" from http://rosettacode.org/wiki/Walk_a_directory/Recursively#Scala
class RichFile(file: File) {
  
  def children = new Iterable[File] {
    def elements = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
  }
  
  def andTree : Iterable[File] = (
    Seq(file) ++ children.flatMap(child => new RichFile(child).andTree)
  )
}

/** implicitely enrich java.io.File with methods of RichFile */
object RichFile {
  implicit def toRichFile(file: File) = new RichFile(file)
}