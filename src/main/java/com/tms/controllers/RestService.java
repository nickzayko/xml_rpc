package com.tms.controllers;

import com.tms.entity.Id;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Path("/hr.employee")
public class RestService {

    private static String url = "https://tacho.glosav.ru/";
    private static String db = "db115";
    private static String username = "api_test@api.api";
    private static String password = "12345";

    @GET
    @Path("/getEmployeeID")
    @Produces("application/json")
    public Id getEmployeeID() {
        XmlRpcClient client = new XmlRpcClient();
        XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        try {
            common_config.setServerURL(
                    new URL(String.format("%s/xmlrpc/2/common", url)));
            Object version = client.execute(common_config, "version", emptyList());
            System.out.println("server version = " + version);

            Object userUID = client.execute(
                    common_config, "authenticate",  asList(db, username, password, emptyMap()));
            System.out.println("userUID = " + userUID);
            XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                }});
            }};

            Id listIdEmployee = new Id();

            List ids = asList((Object[])models.execute(
                    "execute_kw", asList(
                            db, userUID, password,
                            "hr.employee", "search",
                            asList(asList(
                            )),
                            new HashMap() {{ ; }})));
            System.out.println("id of drivers = " +ids);
            listIdEmployee.setId(ids);

            return listIdEmployee;
        } catch (MalformedURLException | XmlRpcException e) {
            e.printStackTrace();
            return null;
        }


    }

}
