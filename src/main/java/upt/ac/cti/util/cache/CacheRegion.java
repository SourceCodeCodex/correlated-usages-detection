package upt.ac.cti.util.cache;

public enum CacheRegion {
  HIERARCHY("hierarchy"), BINDING("binding"), PARSER("parsing"), SEARCH("search"), ALL_TYPES(
      "alltypes"), W_BINDING("writingbinding"), COVERED_TYPES_CNS(
          "coveredtypescns"), COVERED_TYPES_SNS("coveredtypessns"), COVERED_TYPES_CFI(
              "coveredtypescfi"), COVERED_TYPES_SFI("coveredtypessfi");



  final String region;

  private CacheRegion(String region) {
    this.region = region;
  }

}
