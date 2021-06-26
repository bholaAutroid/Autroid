package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class BookedPackagesBE {
   private String name,description,id,category,type;
   @SerializedName("package")
   private String packages;

   public String getPackages() {
      return packages;
   }

   public void setPackages(String packages) {
      this.packages = packages;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }
}
