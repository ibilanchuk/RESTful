/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.Service;

import com.mycompany.restful.dao.FeaturesDao;
import com.mycompany.restful.model.Features;
import com.mysql.jdbc.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author ibilanchuk
 */
@Path("service")
public class Service extends HttpServlet{

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Service
     */
    public Service() {
    }
    private FeaturesDao featureDao = new FeaturesDao();
   

    @GET
    @Path("/Feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFeatureById(@PathParam("id") int id) {
        Features feature = featureDao.getFeatureById(id);
        return Response.status(200).entity(feature).build();
    }


    @GET
    @Path("/Features")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getFeatures( @QueryParam("length")int length, @QueryParam("start") int start) {     
        return featureDao.getFeatures(length,start);
    }   
    @GET
    @Path("/RenderingEngine")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenderingEngine() {
        List<Features> output = featureDao.getRenderingEngine();
        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("/Feature")
    @Produces(MediaType.APPLICATION_JSON)
    public String NewFeature(Features feature) {
        if (!featureDao.saveFeature(feature)) {
            return "{\"response\": \"success\"}";
        }
        return "{\"error\": \"fail\"}";
    }

    @PUT
    @Path("/Feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateFeature(@PathParam("id") int id,Features feature) {
      
        if (!featureDao.updateFeature(id,feature)) {
           return "{\"response\": \"success\"}";
        }
        return "{\"error\": \"fail\"}";
    }

    @DELETE
    @Path("/Feature/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFeature(@PathParam("id") int id) {
        String output = "success";
        if (!featureDao.deleteFeature(id)) {
            return Response.status(200).entity(output).build();

        } else {
            output = "Error";
            return Response.status(304).entity(output).build();
        }
    }

}
