package com.caogen.blog.index;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.caogen.blog.enums.PageEnum;
import com.caogen.blog.enums.RedisKeyEnum;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.caogen.blog.entity.Blog;
import com.caogen.blog.service.BlogService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class SolrIndex {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private SolrClient solrClient;

	@Resource
	private BlogService blogService;

	@Resource
	private JedisPool jedisPool;

	@Value("${qiniuyun.url}")
	private String url;

	/**
	 * 往索引库添加文档
	 * 
	 * @param blogId
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void addBlogDocByBlogId(int blogId) throws SolrServerException, IOException {
		SolrInputDocument document = new SolrInputDocument();
		
		Blog blog = blogService.getBlogInfoById(blogId);
		List<String> tagList = blogService.getTagListById(blogId);
		
		String tag = "";
		if(tagList != null && !tagList.isEmpty()){
			int size = tagList.size();
			for(int i = 0; i < size; i++){
				tag += tagList.get(i) + ",";
			}
		}
		
		document.addField("id", blog.getBlogId());
		document.addField("blogName", blog.getBlogName());
		document.addField("blogImg", blog.getBlogImg());
		document.addField("introduction", blog.getIntroduction());
		document.addField("time", blog.getTime());
		document.addField("blogType", blog.getBlogType());
		document.addField("tag", tag);

		solrClient.add(document);
		solrClient.commit();
		
	}
	
	/**
	 * 往索引库添加文档
	 * 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void addBlogDoc() throws SolrServerException, IOException {
		
		List<Blog> blogList = blogService.getAllBlog();
		List<HashMap<String, Object>> tagList = blogService.getTagList();
		List<SolrInputDocument> documentList = new ArrayList<>();
		
		Blog blog;
		SolrInputDocument document;
		int blogSize = blogList.size();
		for(int i = 0; i < blogSize; i++){
			blog = blogList.get(i);
			document = new SolrInputDocument();
			document.addField("id", blog.getBlogId());
			document.addField("blogName", blog.getBlogName());
			document.addField("blogImg", blog.getBlogImg());
			document.addField("introduction", blog.getIntroduction());
			document.addField("time", blog.getTime());
			document.addField("blogType", blog.getBlogType());
			
			String tag = "";
			if(tagList != null && !tagList.isEmpty()){
				int size = tagList.size();
				for(int j = 0; j < size; j++){
					if(String.valueOf(blog.getBlogId()).equals(tagList.get(j).get("blogId").toString())){
						tag += tagList.get(j).get("tagName") + ",";
					}
				}
			}
			
			document.addField("tag", tag);
			documentList.add(document);
		}
		
		if(!documentList.isEmpty()){
			int dockmentSize = documentList.size();
			for(int k = 0; k < dockmentSize; k++){
				solrClient.add(documentList.get(k));
			}
		}
		solrClient.commit();
		
	}

	/**
	 * 根据id从索引库删除文档
	 * 
	 * @param blogId
	 * @throws IOException
	 * @throws SolrServerException
	 */
	public void deleteDocumentByBlogId(String blogId) throws SolrServerException, IOException {
		solrClient.deleteById(blogId);
		solrClient.commit();
		
	}

	/**
	 * 删除索引库所有文档
	 * 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public void deleteDocument() throws SolrServerException, IOException {
		solrClient.deleteByQuery("*:*");
		solrClient.commit();
		
	}
	
	/**
	 * 获取博客列表的页数
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public long getBlogPage(String keywords) throws SolrServerException, IOException{
		
		SolrQuery solrQuery = new SolrQuery();

		solrQuery.set("q", keywords);
		solrQuery.setSort("id", SolrQuery.ORDER.desc);
		
		// 获取查询结果
		QueryResponse response = solrClient.query(solrQuery);
		// 查询得到文档的集合
		SolrDocumentList solrDocumentList = response.getResults();
		logger.info("通过文档集合获取查询的结果");
		logger.info("查询结果的总数量：" + solrDocumentList.getNumFound());
		
		long page = (solrDocumentList.getNumFound()-1) / PageEnum.blogPageSize.getPageSize() + 1;//这样就计算好了页码数量，逢1进1

		solrClient.commit();
		
		return page;
	}

	/**
	 * 博客搜索
	 * 
	 * @param keywords	关键字
	 * @param page		页数
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public List<HashMap<String, Object>> querySolr(String keywords, int page) throws SolrServerException, Exception {
		String blogScoreKey = RedisKeyEnum.BLOGSCORE.toString();
		List<HashMap<String, Object>> list = new ArrayList<>();
		
		SolrQuery solrQuery = new SolrQuery();

		solrQuery.set("q", keywords);
		solrQuery.setSort("id", SolrQuery.ORDER.desc);
		//searchText这个字段是那五个高亮字段合并而成的，设置成指定字段之后高亮效果更好
		solrQuery.set("df", "searchText");
		
		// 设置分页参数
		solrQuery.setStart(PageEnum.blogPageSize.getPageSize() * (page - 1));
		solrQuery.setRows(PageEnum.blogPageSize.getPageSize());// 每一页多少值
		
		// 开启高亮组件或用query.setParam("hl", "true");  
		solrQuery.setHighlight(true); 
		// 高亮字段  
		solrQuery.addHighlightField("blogName, introduction, blogType, tag");// 高亮字段
		solrQuery.set("hl.usePhraseHighlighter","true");
		solrQuery.set("hl.highlightMultiTerm","true");//使用通配符时,能匹配上的都使用高亮
		solrQuery.set("hl.fragsize", 0);		//返回的最大字符数。默认是100.如果为0，那么该字段不会被fragmented且整个字段的值会被返回。
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		
		// 获取查询结果
		QueryResponse response = solrClient.query(solrQuery);
		// 查询得到文档的集合
		SolrDocumentList solrDocumentList = response.getResults();
		
		if(solrDocumentList.getNumFound() <= 0)return new ArrayList<>();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
		
		Blog blog;
		List<String> tagList;
		HashMap<String, Object> param;
		Map<String, Map<String, List<String>>> maplist = response.getHighlighting();
	    //遍历列表 返回高亮之后的结果..
	    for(SolrDocument solrDocument:solrDocumentList){
	        Object id = solrDocument.get("id");
	        Map<String, List<String>>  fieldMap = maplist.get(id);
	        
	        param = new HashMap<>();
	        blog = new Blog();
	        
	        blog.setBlogId(Integer.parseInt(id.toString()));
	        if(fieldMap.get("blogName") != null){
	        	blog.setBlogName(fieldMap.get("blogName").toString().replace("[", "").replace("]", ""));
	        }else{
	        	blog.setBlogName(solrDocument.get("blogName").toString());
	        }

	        if(fieldMap.get("introduction") != null){
	        	blog.setIntroduction(fieldMap.get("introduction").toString().replace("[", "").replace("]", ""));
	        }else{
	        	blog.setIntroduction(solrDocument.get("introduction").toString());
	        }

			if(fieldMap.get("blogType") != null){
				blog.setBlogType(fieldMap.get("blogType").toString().replace("[", "").replace("]", ""));
			}else{
				blog.setBlogType(solrDocument.get("blogType").toString());
			}

			String[] tags = null;
			if(fieldMap.get("tag") != null){
				tags = fieldMap.get("tag").toString().replace("[", "").replace("]", "").split(",");
			}else{
				tags = solrDocument.get("tag").toString().split(",");
			}
			tagList = Arrays.asList(tags);
	        
			blog.setBlogImg(solrDocument.get("blogImg").toString());
			blog.setTime(sdf.parse(solrDocument.get("time").toString()));

	        
	        Jedis jedis = jedisPool.getResource();
	        blog.setBrowse((int) new Double(jedis.zscore(blogScoreKey, id.toString())==null ? 0 : jedis.zscore(blogScoreKey, id.toString())).longValue());
			blog.setBlogImg(url + blog.getBlogImg());
			jedis.close();
	        
	        param.put("blog", blog);
			param.put("tagList", tagList);
			list.add(param);
	    }

		solrClient.commit();
		
	    return list;
	}
}
