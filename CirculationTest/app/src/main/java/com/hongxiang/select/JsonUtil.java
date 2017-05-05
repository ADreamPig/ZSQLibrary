package com.hongxiang.select;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtil {

   private static Gson gson = null;

   static {
      if (gson == null) {
         gson = new Gson();
      }
   }

   private JsonUtil() {
   }

   /**
    * 将对象转换成json格式
    *
    * @param ts
    * @return
    */
   public static String objectToJson(Object ts) {
      String jsonStr = null;
      if (gson != null) {
         jsonStr = gson.toJson(ts);
      }
      return jsonStr;
   }

   /**
    * 将对象转换成json格式(并自定义日期格式)
    *
    * @param ts
    * @return
    */
   public static String objectToJsonDateSerializer(Object ts, final String dateformat) {
      String jsonStr = null;
      gson = new GsonBuilder().registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
                 public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                    SimpleDateFormat format = new SimpleDateFormat(dateformat);
                    return new JsonPrimitive(format.format(src));
                 }
              }).setDateFormat(dateformat).create();
      if (gson != null) {
         jsonStr = gson.toJson(ts);
      }
      return jsonStr;
   }

   /**
    * 将json格式转换成list对象
    *
    * @param jsonStr
    * @return
    */
   public static List<?> jsonToList(String jsonStr) {
      List<?> objList = null;
      try {
         if (gson != null) {
            Type type = new TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return objList;
   }

   /**
    *
    * @param json
    * @param type
     * @return
     */
   public static Object json2List(String json, Type type) {
      if (gson != null) {
         return gson.fromJson(json, type);
      }
      return null;
   }

   /**
    * 将json格式转换成map对象
    *
    * @param jsonStr
    * @return
    */
   public static Map<?, ?> jsonToMap(String jsonStr) {
      Map<?, ?> objMap = null;
      try {
         if (gson != null) {
            try {
               Type type = new TypeToken<Map<?, ?>>() {
               }.getType();
               objMap = gson.fromJson(jsonStr, type);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return objMap;
   }

   /**
    * 将json转换成bean对象
    *
    * @param jsonStr
    * @return
    */
   public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
      if (isNull(jsonStr)){
         return null;
      }
      T obj = null;
      if (gson != null) {
         obj = gson.fromJson(jsonStr, cl);
      }
      return obj;
   }

   /**
    * 将json转换成bean对象
    *
    * @param jsonStr
    * @param cl
    * @return
    */
   @SuppressWarnings("unchecked")
   public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl, final String pattern) {
      Object obj = null;
      gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
         public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            String dateStr = json.getAsString();
            try {
               return format.parse(dateStr);
            } catch (ParseException e) {
               e.printStackTrace();
            }
            return null;
         }
      }).setDateFormat(pattern).create();
      if (gson != null) {
         obj = gson.fromJson(jsonStr, cl);
      }
      return (T) obj;
   }

   /**
    * 根据
    *
    * @param jsonStr
    * @param key
    * @return
    */
   public static Object getJsonValue(String jsonStr, String key) {
      Object rulsObj = null;
      Map<?, ?> rulsMap = jsonToMap(jsonStr);
      if (rulsMap != null && rulsMap.size() > 0) {
         rulsObj = rulsMap.get(key);
      }
      return rulsObj;
   }

   /**
    * 将json字符串转为list
    * @param s
    * @param clazz
    * @param <T>
     * @return
     */
   public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
      if (isNull(s)){
         return null;
      }
      T[] arr = new Gson().fromJson(s, clazz);

      return new ArrayList<>(Arrays.asList(arr));
   }



   /**
    * 判断是否为null、空字符串或者是"null"
    *
    * @param str
    * @return
    */
   public static boolean isNull(CharSequence... str) {

      for (CharSequence cha : str) {
         if (cha == null || cha.length() == 0 || cha.equals("null")) {
            return true;
         } else {
            return false;
         }
      }

      return true;
   }
}
