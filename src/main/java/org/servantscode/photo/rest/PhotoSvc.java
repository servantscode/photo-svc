package org.servantscode.photo.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.servantscode.commons.rest.SCServiceBase;
import org.servantscode.photo.Photo;
import org.servantscode.photo.db.PhotoDB;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.servantscode.commons.StringUtils.isEmpty;

@Path("/photo")
public class PhotoSvc extends SCServiceBase {
    private static final Logger LOG = LogManager.getLogger(PhotoSvc.class);

    private PhotoDB db;

    public PhotoSvc() {
        db = new PhotoDB();
    }

    @GET @Path("/{guid}")
    public Response getPhoto(@PathParam("guid") String guid) {
        verifyUserAccess("photo.read");

        if(isEmpty(guid))
            throw new NotFoundException();

        Photo photo = db.getPhoto(guid);
        if(photo == null)
            throw new NotFoundException();

        return Response.ok(photo.getBytes(), photo.getFileType()).build();
    }

    @POST
    @Consumes({"image/jpg", "image/png", "image/gif"})
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> storePhoto(@HeaderParam("Content-Type") String fileType,
                                          InputStream photoBypes) {
        verifyUserAccess("photo.create");

        if(isEmpty(fileType))
            throw new NotAcceptableException();

        String guid = UUID.randomUUID().toString();
        db.savePhoto(new Photo(guid, fileType, photoBypes));

        Map<String, Object> resp = new HashMap<>(2);
        resp.put("success", true);
        resp.put("guid", guid);
        return resp;
    }

    @DELETE @Path("/{guid}") @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> deletePhoto(@PathParam("guid") String guid) {
        verifyUserAccess("photo.delete");

        if(isEmpty(guid))
            throw new NotFoundException();

        db.deletePhoto(guid);
        return Collections.singletonMap("success", true);
    }
}
