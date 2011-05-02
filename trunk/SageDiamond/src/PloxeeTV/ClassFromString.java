/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PloxeeTV;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;


/**
 *
 * @author SBANTA
 */
public class ClassFromString {

    static private final Logger LOG = Logger.getLogger(ClassFromString.class);


    public static String GetSortMethod(String SortToGet) {
	 String returnValue = "";
     
              

	            /*
	             * Step 3: Load the class
	             */
	            Class myClass = PloxeeTV.SortMethods.class;
        try {
            /*
             *Step 4: create a new instance of that class
             */
            Object whatInstance = myClass.newInstance();

                   
                    String SParameter = SortToGet;

            /*
             * Step 5: get the method, with proper parameter signature.
             * The second parameter is the parameter type.
             * There can be multiple parameters for the method we are trying to call,
             * hence the use of array.
             */
            Field test = myClass.getField(SortToGet);
            String Value = (String) test.get(whatInstance);
             return Value;
        }

         catch (InstantiationException ex) {
            LOG.fatal(ClassFromString.class.getName()+ex);
        } catch (IllegalAccessException ex) {
            LOG.fatal(ClassFromString.class.getName()+ex);
        }
        catch (NoSuchFieldException ex) {
           LOG.fatal(ClassFromString.class.getName()+ex);
        } catch (SecurityException ex) {
            LOG.fatal(ClassFromString.class.getName()+ex);
        }
            
	           
	     
     
               
         return "";

	    }

    public static Object GetDateClass(String method,Object Airing) {
	 Object returnValue = null;
        try {
	               /*
	             * Step 3: Load the class
	             */
	            Class myClass = PloxeeTV.MetadataCalls.class;

	            /*
	             *Step 4: create a new instance of that class
	             */
	            Object whatInstance = myClass.newInstance();

	           Object methodParameter = Airing;
	            /*
	             * Step 5: get the method, with proper parameter signature.
	             * The second parameter is the parameter type.
	             * There can be multiple parameters for the method we are trying to call,
	             * hence the use of array.
	             */

	            Method myMethod = myClass.getMethod(method,
	                    new Class[] { Object.class });


	            /*
	             *Step 6:
	             *Calling the real method. Passing methodParameter as
	             *parameter. You can pass multiple parameters based on
	             *the signature of the method you are calling. Hence
	             *there is an array.
	             */
	           returnValue =  myMethod.invoke(whatInstance,
	                    new Object[] { methodParameter });

	            LOG.trace("The value returned make class method:"
	                    + returnValue);

	        } catch (SecurityException e) {
	            LOG.fatal(ClassFromString.class.getName()+e);
	        } catch (IllegalArgumentException e) {
	             LOG.fatal(ClassFromString.class.getName()+e);

	        } catch (InstantiationException e) {
	            LOG.fatal(ClassFromString.class.getName()+e);
	        } catch (IllegalAccessException e) {
	             LOG.fatal(ClassFromString.class.getName()+e);
	        } catch (NoSuchMethodException e) {
	            LOG.fatal(ClassFromString.class.getName()+e);
	        } catch (InvocationTargetException e) {
	            LOG.fatal(ClassFromString.class.getName()+e);
	        }
         return returnValue;

	    }
}





   