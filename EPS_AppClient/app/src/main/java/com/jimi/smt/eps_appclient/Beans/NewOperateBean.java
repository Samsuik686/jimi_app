package com.jimi.smt.eps_appclient.Beans;

import com.jimi.smt.eps_appclient.Unit.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewOperateBean {
    /**
     * materialNo : 03.01.0355
     * oldMaterialNo : 03.01.0355
     * operator : A001
     * type : 5
     * firstCheckAllResult : 1
     * boardType : 0
     * line : 302
     * lineseat : ["01-01","01-03","01-04"]
     * programId : 33f50eedce974748b54e094da322e98d
     * remark : 首检成功
     * result : PASS
     * scanlineseat : 01-16
     * workOrder : new_test
     */
//lineseat
    private String materialNo;

    private String operator;
    private int type;
    private Integer storeIssueResult;
    private Integer feedResult;
    private Integer changeResult;
    private Integer checkResult;
    private Integer checkAllResult;
    private Integer firstCheckAllResult;
    private int boardType;
    private String line;
    private String programId;
    private String remark;
    private String result;

    private String workOrder;
    private String [] lineseat;
    String materialId;
    String cycle;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String[] getLineseat() {
        return lineseat;
    }

    public void setLineseat(String[] lineseat) {
        this.lineseat = lineseat;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }



    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getStoreIssueResult() {
        return storeIssueResult;
    }

    public void setStoreIssueResult(Integer storeIssueResult) {
        this.storeIssueResult = storeIssueResult;
    }

    public Integer getFeedResult() {
        return feedResult;
    }

    public void setFeedResult(Integer feedResult) {
        this.feedResult = feedResult;
    }

    public Integer getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(Integer changeResult) {
        this.changeResult = changeResult;
    }

    public Integer getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(Integer checkResult) {
        this.checkResult = checkResult;
    }

    public Integer getCheckAllResult() {
        return checkAllResult;
    }

    public void setCheckAllResult(Integer checkAllResult) {
        this.checkAllResult = checkAllResult;
    }

    public Integer getFirstCheckAllResult() {
        return firstCheckAllResult;
    }

    public void setFirstCheckAllResult(Integer firstCheckAllResult) {
        this.firstCheckAllResult = firstCheckAllResult;
    }

    public int getBoardType() {
        return boardType;
    }

    public void setBoardType(int boardType) {
        this.boardType = boardType;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }



    public static NewOperateBean getOperateObj(String operator, int type , String[] lineseatList, Material.MaterialBean bean)
    {
        NewOperateBean operate = new NewOperateBean();
        String result = bean.getResult();
        operate.setOperator(operator);
        operate.setType(type);
        operate.setProgramId(bean.getProgramId());
        operate.setLine(bean.getLine());
        operate.setWorkOrder(bean.getWorkOrder());
        operate.setBoardType(bean.getBoardType());

        operate.setLineseat(lineseatList);
        operate.setMaterialNo(bean.getScanMaterial());

        operate.setRemark(bean.getRemark());
        operate.setResult(result);
        int typeRes = 0;
        if (result.equalsIgnoreCase("PASS"))
        {
            typeRes = 1;
        }else if (result.equalsIgnoreCase("FAIL"))
        {
            typeRes = 0;
        }
        switch (type)
        {
            case Constants.FEEDMATERIAL:
                operate.setFeedResult(typeRes);
                break;

            case Constants.CHANGEMATERIAL:
                if (typeRes == 1)
                {
                    typeRes = 4;
                }
                operate.setChangeResult(typeRes);
                break;

            case Constants.CHECKMATERIAL:
                operate.setCheckResult(typeRes);
                break;

            case Constants.CHECKALLMATERIAL:
                operate.setCheckAllResult(typeRes);
                break;

            case Constants.STORE_ISSUE:
                operate.setStoreIssueResult(typeRes);
                break;

            case Constants.FIRST_CHECK_ALL:
                operate.setFirstCheckAllResult(typeRes);
                break;
        }
        return operate;
    }

}
