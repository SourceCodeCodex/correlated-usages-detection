package upt.se.utils.builders;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.XEntity;

public class GroupBuilder {

  public static <T extends XEntity> Group<T> create(java.util.List<T> list) {
    Group<T> group = new Group<>();
    group.addAll(list);

    return group;
  }

}
