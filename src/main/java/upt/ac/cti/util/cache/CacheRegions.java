package upt.ac.cti.util.cache;

public enum CacheRegions {
  HIERARCHY("hierarchy"), BINDING("binding"), PARSER("parsing"), SEARCH("search"), ALL_TYPES(
      "alltypes"), COVERED_TYPES_FLOW_INSENSITIVE("coveredtypesfi"), W_BINDING(
          "writingbinding"), COVERED_TYPES_NAME_SIMILARITY("coveredtypesns");



  final String region;

  private CacheRegions(String region) {
    this.region = region;
  }

}
