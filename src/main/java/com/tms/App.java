package com.tms;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String url = "https://tacho.glosav.ru/";
    private static String db = "db115";
    private static String username = "api_test@api.api";
    private static String password = "12345";

    public static void main( String[] args )
    {
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



            Object execute = models.execute("execute_kw", asList(
                    db, userUID, password,
                    "hr.employee", "check_access_rights",
                    asList("read"),
                    new HashMap() {{
                        put("raise_exception", false);
                    }}
            ));

            System.out.println(execute);

            List ids = asList((Object[])models.execute(
                    "execute_kw", asList(
                            db, userUID, password,
                            "hr.employee", "search",
                            asList(asList(
                                    )),
                            new HashMap() {{ ; }})));

            System.out.println("id of drivers = " +ids);


            Object execute1 = models.execute("execute_kw", asList(
                    db, userUID, password,
                    "hr.employee", "fields_get",
                    emptyList(),
                    new HashMap() {{
                        put("attributes", asList("string", "help", "type"));
                    }}
            ));
            System.out.println("-------------------------Attributes--------------------------");
            System.out.println(execute1);


            System.out.println("-------------------------Drivers--------------------------");
            List<Object> objects = asList((Object[]) models.execute("execute_kw", asList(
                    db, userUID, password,
                    "hr.employee", "search",
                    asList(asList(
                            asList("work_driving_summary", ">", 45000)
                    ))
            )));

            for (Object object : objects) {
                System.out.println(object);
            }


//            final Map record = (Map)((Object[])models.execute(
//                    "execute_kw", asList(
//                            db, userUID, password,
//                            "hr.employee", "read",
//                            asList(ids)
//                    )
//            ))[0];
// count the number of fields fetched by default
//            record.size();
//            System.out.println("param = " + record.size());


            List<Object> driversDetail = asList((Object[]) models.execute("execute_kw", asList(
                    db, userUID, password,
                    "hr.employee", "search_read",
                    asList(asList(
                            asList("id", "=", 130)
                    ))
            )));

            System.out.println("driver : " + driversDetail);

        } catch (MalformedURLException | XmlRpcException e) {
            e.printStackTrace();
        }

    }
}



