package cn.qianzf.widget.service;

import cn.qianzf.widget.letex.ExtendCommands;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.scilab.forge.jlatexmath.DefaultTeXFont;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.scilab.forge.jlatexmath.cyrillic.CyrillicRegistration;
import org.scilab.forge.jlatexmath.greek.GreekRegistration;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class GeneratePicService {

    private int defaultSize = 14;

    @PostConstruct
    public void initService() {
        ExtendCommands.regPreExtCommands();
    }


    /**
     * latex生成图片
     *
     * @param latex
     * @return 图片字节码
     * @throws IOException
     */
    public byte[] toPng(String latex) throws IOException {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, defaultSize);
        icon.setInsets(new Insets(2, 2, 2, 2));
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        bufferedImage = g2.getDeviceConfiguration().createCompatibleImage(icon.getIconWidth(), icon.getIconHeight(), Transparency.TRANSLUCENT);
        g2 = bufferedImage.createGraphics();
        g2.setBackground(Color.white);
        g2.clearRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        g2.setColor(new Color(0, 0, 0));
        icon.paintIcon(null, g2, 0, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        return baos.toByteArray();
    }


    /**
     * 转成svg
     *
     * @param latex
     * @return
     * @throws Exception
     */
    public byte[] toSvg(String latex) throws Exception {
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
        SVGGraphics2D g2 = new SVGGraphics2D(ctx, true);
        DefaultTeXFont.registerAlphabet(new CyrillicRegistration());
        DefaultTeXFont.registerAlphabet(new GreekRegistration());
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, defaultSize);
        icon.setInsets(new Insets(2, 2, 2, 2));
        g2.setSVGCanvasSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        g2.setColor(Color.white);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        icon.paintIcon(null, g2, 0, 0);
        ByteArrayOutputStream svgs = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(svgs, "UTF-8");
        g2.stream(out, false,true);
        svgs.close();
        return svgs.toByteArray();
    }

    public byte[] svgToPng(byte[] svg) throws Exception {
        return svgToPng(svg, null);
    }

    /**
     * svg 转成png图片
     *
     * @param svg
     * @param dimension 宽高
     * @return
     * @throws Exception
     */
    public byte[] svgToPng(byte[] svg, float[] dimension) throws Exception {
        InputStream svgIs = new ByteArrayInputStream(svg);
        TranscoderInput input = new TranscoderInput(svgIs);
        PNGTranscoder transcoder = new PNGTranscoder();
        if (dimension != null) {
            TranscodingHints transcodingHints = transcoder.getTranscodingHints();
            transcodingHints.put(PNGTranscoder.KEY_WIDTH, dimension[0]);
            transcodingHints.put(PNGTranscoder.KEY_HEIGHT, dimension[1]);
            transcoder.setTranscodingHints(transcodingHints);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(baos);
        transcoder.transcode(input, output);
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }

    public byte[] replaceSvgIndent(byte[] bs) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                InputSource is = new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
                return is;
            }
        });
        Document document = builder.parse(new ByteArrayInputStream(bs));
        TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty("encoding", "UTF-8");
        DOMSource source = new DOMSource(document);
        ByteArrayOutputStream svgs = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(svgs);
        transformer.transform(source, result);
        bs=svgs.toByteArray();
        return bs;
    }
}
