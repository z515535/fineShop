package com.fineShop.search.mapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.fineShop.search.mapper.adapter.ScanXmlAdapter;
/**
* @author 作者 wugf:
* @version 创建时间：2017年3月26日 下午10:50:44<p>
* 类说明		扫描加载xml配置文件管理器
* 
*/
public class ScanXmlManager implements ScanXmlAdapter{
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@SuppressWarnings("unchecked")
	public Set<XmlConfigure> scanAndLoadConfig(String mapperLocations) {
		//加载xml文件路径
		Set<String> pathSet = getAllXmlPath(mapperLocations);
		
		//解析xml文件
		Set<XmlConfigure> xmlConfigureSet = new LinkedHashSet<XmlConfigure>(pathSet.size());
		SAXBuilder bulider = new SAXBuilder();
		InputStream in = null;
		XmlConfigure xmlConfigure = null;
		Sentence sentence = null;
		for (String path : pathSet) {
			try {
				xmlConfigure = new XmlConfigure();
				in = new FileInputStream(path);
				Document document = bulider.build(in);
				Element root = document.getRootElement();		//获取根节点对象
				xmlConfigure.setNamespace(root.getAttributeValue(Constant.NAME_SPACE));
				xmlConfigure.setIndex(root.getAttributeValue(Constant.INDEX));
				xmlConfigure.setType(root.getAttributeValue(Constant.TYPE));
				
				List<Element> elementList = root.getChildren();
				Set<Sentence> sentenceSet = new LinkedHashSet<Sentence>(elementList.size());
				for (Element element : elementList) {
					sentence = new Sentence();
					sentence.setActionType(CommandType.formatIndex(element.getName()));
					sentence.setMethodName(element.getAttributeValue(Constant.ID));
					sentence.setIndex(element.getAttributeValue(Constant.INDEX));
					sentence.setType(element.getAttributeValue(Constant.TYPE));
					sentence.setResource(element.getValue());
					sentenceSet.add(sentence);
				}
				xmlConfigure.setSentences(sentenceSet);
				xmlConfigureSet.add(xmlConfigure);
			} catch (Exception e) {
				System.err.println("解析xml 映射文件失败");
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return xmlConfigureSet;
	}

	/**
	 * 加载配置目录下的所有文件路径
	 * @return
	 */
	private Set<String> getAllXmlPath(String mapperLocations){
		Set<String> xmlPathSet = new LinkedHashSet<String>();
		try {
			Resource[] resources = this.resourcePatternResolver.getResources(mapperLocations);
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				String xmlPath = resource.getURI().getPath();
				xmlPathSet.add(xmlPath);
			}
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
		} 
		return xmlPathSet;
	}
}
