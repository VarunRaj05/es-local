package ja.com

import java.io.{File, FileInputStream}

import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.pdf.PDFParser
import org.apache.tika.sax.BodyContentHandler

object testme {
  def main(args: Array[String]): Unit = {
    /*import org.apache.pdfbox.pdmodel.PDDocument
    import org.apache.pdfbox.text.PDFTextStripper

    val document = PDDocument.load(new File("C:\\Users\\Ja\\Google Drive\\srcfile\\esoutput_json\\testmain.pdf"))

    //Instantiate PDFTextStripper class
    val pdfStripper = new PDFTextStripper

    //Retrieving text from PDF document
    val text = pdfStripper.getText(document)

    println(text)*/



    val handler : BodyContentHandler = new BodyContentHandler();
    val metadata = new org.apache.tika.metadata.Metadata();
    val inputstream = new FileInputStream(new File("C:\\Users\\Ja\\Google Drive\\srcfile\\esoutput_json\\testmain.pdf"));

    val pcontext = new ParseContext();

    //parsing the document using PDF parser
    val pdfparser = new PDFParser();
    pdfparser.parse(inputstream, handler, metadata,pcontext);

    //getting the content of the document
    println("Contents of the PDF :" + handler.toString());

    //getting metadata of the document
    println("Metadata of the PDF:");
    val metadataNames = metadata.names();

    for(name <- metadataNames) {
      println(name+ " : " + metadata.get(name));
    }


  }

}