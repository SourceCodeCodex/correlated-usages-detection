package upt.ac.cti.util.cache;

public enum CacheRegions {
  HIERARCHY("hierarchy"), BINDING("binding"), PARSER("parsing"), SEARCH("search"), ALL_TYPES(
      "alltypes"), COVERED_TYPES("coveredtypes"), W_BINDING("writingbinding");



  final String region;

  private CacheRegions(String region) {
    this.region = region;
  }

}
