package ja.Tikamanager

/*
package ja.com;

import org.apache.pdfbox.pdfparser.PDFParser;

import java.io.FileInputStream;

*/

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.html.HtmlParser
import org.apache.tika.parser.microsoft.OfficeParser
import org.apache.tika.parser.odf.OpenDocumentParser
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.parser.{AutoDetectParser, ParseContext}
import org.apache.tika.sax.{BodyContentHandler, LinkContentHandler, TeeContentHandler, ToHTMLContentHandler}

/**
  * Created by Ja on 31/05/2017.
  */
object jatikaparser {

  def parseTextFile(bytes: Array[Byte]) :String = {
    val parser :  AutoDetectParser  = new AutoDetectParser();
    val handler :    BodyContentHandler  = new BodyContentHandler();
    val metadata  :    Metadata = new Metadata();
    import java.io.{ByteArrayInputStream, InputStream}
    val stream: InputStream = new ByteArrayInputStream(bytes)
    parser.parse(stream, handler, metadata);
    return handler.toString();
  }
 /* def parsePdfFile(bytes: Array[Byte]) :String = {
    var retValue = ""
    try {

      val handler: BodyContentHandler = new BodyContentHandler();
      val metadata: Metadata = new Metadata();
      val pcontext: ParseContext = new ParseContext()

      import java.io.{ByteArrayInputStream, InputStream}
      val stream: InputStream = new ByteArrayInputStream(bytes)

      //parsing the document using PDF parser
      val pdfparser: PDFParser = new PDFParser()
      pdfparser.parse(stream, handler, metadata, pcontext)
      println("Metadata of the PDF:");
      val metadataNames = metadata.names();

      for (name <- metadataNames)
        retValue += name + " : " + metadata.get(name) + "\n"

      retValue += handler.toString
    }
    catch
      {
        case e: Exception => {
          println("Error**********************************************************************Begin")
          println("Error in pdf parser:" + e.getMessage)
          println(new String(bytes))
          println("Error**********************************************************************End")
        }

      }
    return retValue
  }*/



  def parsePdfFileWithpbox(bytes: Array[Byte]) : String = {
    var retvalue = ""

    try {
      import java.io.{ByteArrayInputStream, InputStream}
      val stream: InputStream = new ByteArrayInputStream(bytes)

      val parser: org.apache.pdfbox.pdfparser.PDFStreamParser = new org.apache.pdfbox.pdfparser.PDFStreamParser(bytes)
      parser.parse()

      retvalue = parser.toString
    }
    catch
      {
        case e1: Exception => {
            println("Error parsePdfFileWithpbox**********************************************************************Begin")
            println(e1.getMessage)
            println("Error**********************************************************************End")
          }
      }
    retvalue
  }


  def parsePdfFile(bytes: Array[Byte]) :String = {
    var retValue = ""

    try {
      val handler: BodyContentHandler = new BodyContentHandler();
      val metadata: Metadata = new Metadata();
      val pcontext: ParseContext = new ParseContext()

      import java.io.{ByteArrayInputStream, InputStream}
      val stream: InputStream = new ByteArrayInputStream(bytes)

      //parsing the document using PDF parser
      val pdfparser: PDFParser = new PDFParser()
      pdfparser.parse(stream, handler, metadata, pcontext)
      println("Metadata of the PDF:");
      val metadataNames = metadata.names();

      for (name <- metadataNames)
        retValue += name + " : " + metadata.get(name) + "\n"

      try {
          retValue += handler.toString
      }
      catch {
        case e1: Exception => {
          println("Error while getting the content**********************************************************************Begin")
          println(e1.getMessage)
          println("Error**********************************************************************End")
        }
      }
    }

      catch {
        case e: Exception => {
          println("Error**********************************************************************Begin")
          println("Error in pdf parser:" + e.getMessage)
          println(new String(bytes))
          println("Error**********************************************************************End")
        }
      }
      return retValue

  }

  def parseHtmlFile(bytes: Array[Byte]) :String = {

   // val handler :    BodyContentHandler  = new BodyContentHandler(-1);

    val pcontext : ParseContext  = new ParseContext()

    import java.io.{ByteArrayInputStream, InputStream}

    val stream: InputStream = new ByteArrayInputStream(bytes)
    val linkHandler : LinkContentHandler = new LinkContentHandler();
    val textHandler = new BodyContentHandler(-1);
    val toHTMLHandler : ToHTMLContentHandler = new ToHTMLContentHandler();

    val teeHandler : TeeContentHandler = new TeeContentHandler(linkHandler, textHandler, toHTMLHandler);
    val metadata = new Metadata();
    val parseContext = new ParseContext();
    val parser = new HtmlParser();
    parser.parse(stream, teeHandler, metadata, parseContext);
    var retValue = ""
    retValue += metadata.get("title")
    retValue += textHandler.toString

    retValue

  }

  def parseOdfFile(bytes: Array[Byte]) :String = {
    val retValue = StringBuilder.newBuilder
    val handler :    BodyContentHandler  = new BodyContentHandler();
    val metadata  :    Metadata = new Metadata();
    val pcontext : ParseContext  = new ParseContext()

    import java.io.{ByteArrayInputStream, InputStream}
    val stream: InputStream = new ByteArrayInputStream(bytes)

    //parsing the document using PDF parser
    val openofficeparser: OpenDocumentParser = new OpenDocumentParser ()
    openofficeparser.parse(stream, handler, metadata,pcontext)
    retValue.append("Contents of the ODF document:" + handler.toString() + "\n")
    retValue.append("Metadata of the ODF document:")
    val metadataNames = metadata.names()

    for(name <- metadataNames) {
      retValue.append(name + ": " + metadata.get(name) + "; " + "\n")
    }

    retValue.toString()
  }

  //Open Document Parser
  /*val openofficeparser: OpenDocumentParser = new OpenDocumentParser ();
  openofficeparser.parse(inputstream, handler, metadata,pcontext);
  System.out.println("Contents of the document:" + handler.toString());
  System.out.println("Metadata of the document:");
  String[] metadataNames = metadata.names();

  for(String name : metadataNames) {
    System.out.println(name + " :  " + metadata.get(name));
  }*/

  def parseDocFile(bytes: Array[Byte], xtype : Boolean) :String = {

    val retValue = StringBuilder.newBuilder

    try {
      import org.apache.tika.parser.microsoft.ooxml.OOXMLParser
      val handler: BodyContentHandler = new BodyContentHandler();
      val metadata: Metadata = new Metadata();
      val pcontext: ParseContext = new ParseContext()
      var msofficeparser: org.apache.tika.parser.Parser = null
      import java.io.{ByteArrayInputStream, InputStream}
      val stream: InputStream = new ByteArrayInputStream(bytes)

      if (xtype)
        msofficeparser = new OOXMLParser()
      else
        msofficeparser = new OfficeParser()



      //val msofficeparser : OOXMLParser = new OOXMLParser ();
      msofficeparser.parse(stream, handler, metadata, pcontext);
      retValue.append("Contents of the document:" + handler.toString() + "\n");
      retValue.append("Metadata of the document:");
      val metadataNames = metadata.names();

      for (name <- metadataNames) {
        retValue.append(name + ": " + metadata.get(name) + "; " + "\n")
      }
      retValue.append(handler.toString)
  }
  catch
  {
    case e: Exception => {
      println("Error**********************************************************************Begin")
      println("Error in Doc parser:" + e.getMessage)
      println(new String(bytes))
      println("Error**********************************************************************End")
    }

  }
    return retValue.toString
  }


}


