package cn.qianzf.widget.controller;

import cn.qianzf.widget.service.GeneratePicService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 生成表达式图片
 */
@RequestMapping("generatepic")
@Controller
public class GeneratePicController {

    private Log log = LogFactory.getLog(GeneratePicController.class);

    @Resource
    private GeneratePicService generatePicService;

    @RequestMapping(value = "pngbase64", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String pngBase64(HttpServletRequest request, String latex) {
        try {
            byte[] bs = generatePicService.toPng(latex);
            String base64 = Base64Utils.encodeToString(bs);
            return base64;
        } catch (Exception e) {
            log.error("", e);
            return "ERROR:" + (StringUtils.isEmpty(e.getMessage()) ? e : e.getMessage());
        }
    }

    @RequestMapping(value = "tosvg", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String toSvg(HttpServletRequest request, String latex, String asBase64) {
        try {
            byte[] bs = generatePicService.toSvg(latex);
            //bs = generatePicService.replaceSvgIndent(bs);
            if ("false".equalsIgnoreCase(asBase64)) {
                String svg= new String(bs, "UTF-8");
                return svg.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
            }
            String base64 = Base64Utils.encodeToString(bs);
            return base64;
        } catch (Exception e) {
            log.error("", e);
            return "ERROR:" + (StringUtils.isEmpty(e.getMessage()) ? e : e.getMessage());
        }
    }

}
