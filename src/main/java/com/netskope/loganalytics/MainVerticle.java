package com.netskope.loganalytics;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;



/**Rest Controller
 * 
 * @author nareshkancharla
 *
 */
public class MainVerticle extends AbstractVerticle{
	
	 private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
	
	private static String filePath ="src/main/resources/access.log";
	
	public static void main(String args[]){
		logger.info("....args[0]"+ args[0]);
	}
	
	@Override
	public void start(){
		 Router router = Router.router(vertx);
		 
		 if(config().getString("logpath") != null) {
		      filePath = config().getString("logpath");
		      System.out.println("log path "+ filePath);
		 }
		 
		 router.route().handler(BodyHandler.create());
		 
		 router.get("/log/topurls/:number").handler(this::handleGetProduct);
		 
		 router.get("/log/user-agents/top/:number").handler(this::handleGetProduct);
		 
		 router.get("/log/user-agents").handler(this::handleGetProduct);
		 
		 router.get("/log/GET/count").handler(this::handleGetProduct);
		 
		 router.get("/log/POST/count").handler(this::handleGetProduct);
		 
		 router.get("/log/top/responsecodes").handler(this::handleGetProduct);
		 
		 vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
	
	
	/**
	 *  Handle all HTTP GET calls
	 * @param routingContext
	 */
	private void handleGetProduct(RoutingContext routingContext) {
		logger.info("path params "+ routingContext.request().uri());
		String no = routingContext.request().getParam("number");
		int number = 10;
		if(no != null)
		  number = Integer.parseInt(no);
	    HttpServerResponse response = routingContext.response();
	 
	    	DynamicHandler(routingContext);
	    	//response.putHeader("content-type", "application/json").end(ProcessBuilder.getTopUrls(number));
	   
		
	}
	
	public void DynamicHandler(RoutingContext rc){
		HttpServerResponse response = rc.response();
		
		if(rc.request().uri().indexOf("log/topurls") >=0){
			String no = rc.request().getParam("number");
			int number = Integer.parseInt(no);
			response.putHeader("content-type", "application/json").end(ProcessBuilder.getTopUrls(number, filePath));
		}else if(rc.request().uri().equalsIgnoreCase("/log/user-agents")){
			response.putHeader("content-type", "application/json").end(ProcessBuilder.getAllUniqueAgents(filePath));
		}else if(rc.request().uri().indexOf("log/useragents/top") >=0){
			String no = rc.request().getParam("number");
			int number = Integer.parseInt(no);
			response.putHeader("content-type", "application/json").end(ProcessBuilder.getTopUserAgents(number, filePath));
		}else if(rc.request().uri().indexOf("log/GET/count") >=0){
			response.putHeader("content-type", "application/json").end(ProcessBuilder.getHTTMethodCount("GET", filePath));
	    }else if(rc.request().uri().indexOf("log/POST/count") >=0){
	    	response.putHeader("content-type", "application/json").end(ProcessBuilder.getHTTMethodCount("POST", filePath));
	    }else if(rc.request().uri().indexOf("/log/top/responsecodes") >=0){
	    	response.putHeader("content-type", "application/json").end(ProcessBuilder.getTopResponseCodes(filePath));
	    }else{
	    	logger.error("Failed to find resource for END POINT"+ rc.request().uri().toString());
			sendError(404, response);
		}
	
	}
	
	
	private void sendError(int statusCode, HttpServerResponse response) {
	    response.setStatusCode(statusCode).end();
	  }

}
