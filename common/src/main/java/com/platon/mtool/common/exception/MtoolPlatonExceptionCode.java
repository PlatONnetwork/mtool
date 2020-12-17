package com.platon.mtool.common.exception;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * mtool区块链异常码
 *
 * <p>Created by liyf.
 */
public enum MtoolPlatonExceptionCode implements MtoolExceptionFactory<MtoolPlatonException> {
  SUCESS(0),
  SYSTEM_ERROR(1),
  OBJECT_NOT_FOUND(2),
  PARAM_ERROR(3),
  E301000(301000),
  E301001(301001),
  E301002(301002),
  E301003(301003),
  E301004(301004),
  E301005(301005),
  E301006(301006),
  E301007(301007),
  E301008(301008),
  E301009(301009),
  E301100(301100),
  E301101(301101),
  E301102(301102),
  E301103(301103),
  E301104(301104),
  E301105(301105),
  E301106(301106),
  E301107(301107),
  E301108(301108),
  E301109(301109),
  E301110(301110),
  E301111(301111),
  E301112(301112),
  E301113(301113),
  E301114(301114),
  E301115(301115),
  E301116(301116),
  E301117(301117),
  E301118(301118),
  E301119(301119),
  E301200(301200),
  E301201(301201),
  E301202(301202),
  E301203(301203),
  E301204(301204),
  E301205(301205),
  E302001(302001),
  E302002(302002),
  E302003(302003),
  E302004(302004),
  E302005(302005),
  E302006(302006),
  E302007(302007),
  E302008(302008),
  E302009(302009),
  E302010(302010),
  E302011(302011),
  E302012(302012),
  E302013(302013),
  E302014(302014),
  E302015(302015),
  E302016(302016),
  E302017(302017),
  E302018(302018),
  E302019(302019),
  E302020(302020),
  E302021(302021),
  E302022(302022),
  E302023(302023),
  E302024(302024),
  E302025(302025),
  E302026(302026),
  E302027(302027),
  E302028(302028),
  E302029(302029),
  E302030(302030),
  E302031(302031),
  E302032(302032),
  E302033(302033),
  E302034(302034),
  E303000(303000),
  E303001(303001),
  E303002(303002),
  E303003(303003),
  E303004(303004),
  E303005(303005),
  E303006(303006),
  E303007(303007),
  E303008(303008),
  E303009(303009),
  E303010(303010),
  E304001(304001),
  E304002(304002),
  E304003(304003),
  E304004(304004),
  E304005(304005),
  E304006(304006),
  E304007(304007),
  E304008(304008),
  E304009(304009),
  E304010(304010),
  E304011(304011),
  E304012(304012),
  E304013(304013),
  ;

  private String[] replaceArgs;
  private static final ResourceBundle resourceBundle =
      ResourceBundle.getBundle("PlatonException", Locale.US);
  private Integer code;

  MtoolPlatonExceptionCode(Integer code) {
    this.code = code;
  }

  /**
   * get MtoolPlatonExceptionCode from code.
   *
   * @param code error code
   * @return MtoolPlatonExceptionCode
   */
  public static MtoolPlatonExceptionCode getFromCode(Integer code,String... replaceArgs) {
    for (MtoolPlatonExceptionCode value : MtoolPlatonExceptionCode.values()) {
      if (value.getCode().equals(code)) {
        value.replaceArgs=replaceArgs;
        return value;
      }
    }
    throw new MtoolPlatonException(-1, "Error code not found: " + code);
  }

  public Integer getCode() {
    return code;
  }

  public String getMessage() {
    String msg = resourceBundle.getString(name());
    if(replaceArgs!=null&&replaceArgs.length>0){
      for (int i=0;i<replaceArgs.length;i++){
        msg = msg.replace("%"+i,replaceArgs[i]);
      }
    }
    return msg;
  }

  @Override
  public MtoolPlatonException create() {
    return new MtoolPlatonException(code, getMessage());
  }

  public MtoolPlatonException create(Object... args) {
    return new MtoolPlatonException(code, String.format(getMessage(), args));
  }

  public String[] getReplaceArgs() {
    return replaceArgs;
  }
}
