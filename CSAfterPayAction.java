package weaver.interfaces.workflow.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.math.BigDecimal;
import com.weaver.general.Util;

import nc.ydws.ws.itf.tools.ISendServiceTools.ISendServiceToolsPortTypeProxy;
import weaver.conn.RecordSet;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.DetailTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.Row;

/**
 * 
 * @author profe 2019年11月18日
 *  后付款流程付款接口
 */
public class CSAfterPayAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(CSAfterPayAction.class);
	// 所属集团,最大长度为20,类型为:String
		private static final String pk_group="06";
		//应付财务组织,最大长度为20,类型为:String
		private static final String pk_org="220307000000";
		//是否红冲过,最大长度为1,类型为:UFBoolean
		private static final String isreded="N";
		//单据类型编码,最大长度为50,类型为:String
		private static final String pk_billtype="F3";
		//应付类型code,最大长度为20,类型为:String
		private static final String pk_tradetype="F3-Cxx-01";
		//单据大类,最大长度为2,类型为:String
		private static final String billclass="fk";
		//附件张数,最大长度为0,类型为:Integer
		private static final Integer accessorynum=0;
		//是否流程单据,最大长度为1,类型为:UFBoolean
		private static final String isflowbill="N";
		//期初标志,最大长度为1,类型为:UFBoolean
		private static final String isinit="N";
		//单据所属系统,最大长度为0,类型为:Integer
		private static final Integer syscode=0;
		//单据来源系统,最大长度为0,类型为:Integer
		private static final Integer src_syscode=0;
		//单据状态,最大长度为0,类型为:Integer
		private  static final Integer billstatus = 0;
		//制单人,最大长度为20,类型为:String
		private static final String billmaker="OAFK";
		//生效状态,最大长度为0,类型为:Integer
		private static final Integer effectstatus=0;
		//收货国,最大长度为20,类型为:String
		private static final String rececountryid="CN";
		//报税国,最大长度为20,类型为:String
		private static final String taxcountryid="CN";
		
		private static final String pk_balatype="005";
		private static final String RESULT_CODE="1";
		
	@Override
	public String execute(RequestInfo requestInfo) {
		Date date=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		SimpleDateFormat yearFormat=new SimpleDateFormat("YYYY");
		SimpleDateFormat monthFormat=new SimpleDateFormat("MM");
		String nowdate=dateFormat.format(date);
		String billyear=yearFormat.format(date);
		String billmonth=monthFormat.format(date);
		String billid="";
		BigDecimal money=new BigDecimal(0);
		String supplier="";
		String natureaccount="";
		BigDecimal skje=new BigDecimal(0);
		String recaccount="";
		String remark="";
		String pk_deptid="";
		String ncpaycode="";
		//BigDecimal ratemoney=new BigDecimal(0);
		//BigDecimal noratemoney=new BigDecimal(0);
		BigDecimal sumskje=new BigDecimal(0);
		Property prop[]=requestInfo.getMainTableInfo().getProperty();
		for (int i = 0; i < prop.length; i++) {
			String field=prop[i].getName();
			if(field.equals("lcbh")) {
				billid=Util.null2String(prop[i].getValue());
				logger.info("流程编号:"+billid);
				
			}
			if(field.equals("skhjje")) {
				sumskje=new BigDecimal(Util.null2String(prop[i].getValue()));
				logger.info("收款金额合计:"+sumskje);
			}
			if(field.equals("ncpaycode")) {
				ncpaycode=Util.null2String(prop[i].getValue());
				logger.info("NC付款编号:"+ncpaycode);
						
			}
			
		}
	
		if(!ncpaycode.equals("")&&ncpaycode!=null) {
			return SUCCESS;
		}
		StringBuilder stringBuilder=new StringBuilder();
		
		stringBuilder.append("<?xml version=\"1.0\" encoding='GBK'?>");
		stringBuilder.append("<ufinterface account=\"develop\" billtype=\"F3\" businessunitcode=\"\" filename=\"\" groupcode=\"\" isexchange=\"Y\" orgcode=\"\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"LYSOA\">");
		stringBuilder.append("<bill id=\""+billid+"\">");
		stringBuilder.append("<billhead>");
		stringBuilder.append("<pk_group>"+pk_group+"</pk_group>");
		stringBuilder.append("<pk_pcorg></pk_pcorg>");
		stringBuilder.append("<pk_fiorg>"+pk_org+"</pk_fiorg>");
		stringBuilder.append("<sett_org>"+pk_org+"</sett_org>");
		stringBuilder.append("<isreded>"+isreded+"</isreded>");
		stringBuilder.append("<outbusitype></outbusitype>");
		stringBuilder.append("<payman></payman>");
		stringBuilder.append("<paydate></paydate>");
		stringBuilder.append("<isonlinepay>N</isonlinepay>");
		stringBuilder.append("<officialprintuser></officialprintuser>");
		stringBuilder.append("<officialprintdate></officialprintdate>");
		stringBuilder.append("<modifiedtime></modifiedtime>");
		stringBuilder.append("<settlenum></settlenum>");
		stringBuilder.append("<creationtime>"+nowdate+"</creationtime>");
		stringBuilder.append("<creator>"+billmaker+"</creator>");
		stringBuilder.append("<pk_billtype>"+pk_billtype+"</pk_billtype>");
		stringBuilder.append("<ismandatepay>N</ismandatepay>");
		stringBuilder.append("<custdelegate></custdelegate>");
		stringBuilder.append("<pk_corp></pk_corp>");
		stringBuilder.append("<modifier></modifier>");
		stringBuilder.append("<pk_tradetype>"+pk_tradetype+"</pk_tradetype>");
		stringBuilder.append("<modifier></modifier>");
		stringBuilder.append("<billclass>"+billclass+"</billclass>");
		stringBuilder.append("<pk_paybill></pk_paybill>");
		stringBuilder.append("<accessorynum>"+accessorynum+"</accessorynum>");
		stringBuilder.append("<subjcode></subjcode>");
		stringBuilder.append("<isflowbill>"+isflowbill+"</isflowbill>");
		stringBuilder.append("<confirmuser></confirmuser>");
		stringBuilder.append("<isinit>"+isinit+"</isinit>");
		stringBuilder.append("<billno></billno>");
		stringBuilder.append("<billdate>"+nowdate+"</billdate>");
		stringBuilder.append("<syscode>"+syscode+"</syscode>");
		stringBuilder.append("<src_syscode>"+src_syscode+"</src_syscode>");
		stringBuilder.append("<billstatus>"+billstatus+"</billstatus>");
		stringBuilder.append("<billmaker>"+billmaker+"</billmaker>");
		stringBuilder.append("<approver></approver>");
		stringBuilder.append("<approvedate></approvedate>");
		stringBuilder.append("<isnetpayready>N</isnetpayready>");
		stringBuilder.append("<lastadjustuser></lastadjustuser>");
		stringBuilder.append("<signuser></signuser>");
		stringBuilder.append("<signyear></signyear>");
		stringBuilder.append("<signperiod></signperiod>");
		stringBuilder.append("<signdate></signdate>");
		stringBuilder.append("<pk_busitype></pk_busitype>");
		stringBuilder.append("<money>"+money+"</money>");
		stringBuilder.append("<local_money>"+money+"</local_money>");
		stringBuilder.append("<billyear>"+billyear+"</billyear>");
		stringBuilder.append("<billperiod>"+billmonth+"</billperiod>");
		stringBuilder.append("<scomment></scomment>");
		stringBuilder.append("<pk_busitype></pk_busitype>");
		stringBuilder.append("<settletype>0</settletype>");
		stringBuilder.append("<effectstatus>0</effectstatus>");
		stringBuilder.append("<effectuser></effectuser>");
		stringBuilder.append("<effectdate></effectdate>");
		stringBuilder.append("<lastapproveid></lastapproveid>");
		stringBuilder.append("<def9>"+billid+"</def9>");
		stringBuilder.append("<costcenter></costcenter>");
		stringBuilder.append("<rececountryid>"+rececountryid+"</rececountryid>");
		stringBuilder.append("<taxcountryid>"+taxcountryid+"</taxcountryid>");
		stringBuilder.append("<pk_group>"+pk_group+"</pk_group>");
		stringBuilder.append(" <pk_org>"+pk_org+"</pk_org>");
		stringBuilder.append("<pk_pcorg></pk_pcorg>");
		stringBuilder.append("<pk_fiorg>"+pk_org+"</pk_fiorg>");
		stringBuilder.append(" <sett_org>"+pk_org+"</sett_org>");
		stringBuilder.append("<isreded>"+isreded+"</isreded>");
		stringBuilder.append("<outbusitype></outbusitype>");
		stringBuilder.append("<officialprintuser></officialprintuser>");
		stringBuilder.append("<officialprintuser></officialprintuser>");
		stringBuilder.append("<modifiedtime></modifiedtime>");
		stringBuilder.append("<creationtime>"+nowdate+"</creationtime>");
		stringBuilder.append("<creator>"+billmaker+"</creator>");
		stringBuilder.append("<pk_billtype>"+pk_billtype+"</pk_billtype>");
		stringBuilder.append("<pk_tradetype>"+pk_tradetype+"</pk_tradetype>");
		stringBuilder.append("<modifier></modifier>");
		stringBuilder.append("<billclass>"+billclass+"</billclass>");
		stringBuilder.append("<pk_payablebill></pk_payablebill>");
		stringBuilder.append("<accessorynum>"+accessorynum+"</accessorynum>");
		stringBuilder.append("<subjcode></subjcode>");
		stringBuilder.append("<isflowbill>"+isflowbill+"</isflowbill>");
		stringBuilder.append("<confirmuser></confirmuser>");
		stringBuilder.append("<isinit>"+isinit+"</isinit>");
		stringBuilder.append("<billno></billno>");
		stringBuilder.append("<billdate>"+nowdate+"</billdate>");
		stringBuilder.append("<syscode>"+syscode+"</syscode>");
		stringBuilder.append("<src_syscode>"+src_syscode+"</src_syscode>");
		stringBuilder.append("<billstatus>"+billstatus+"</billstatus>");
		stringBuilder.append("<billmaker>"+billmaker+"</billmaker>");
		stringBuilder.append("<approver></approver>");
		stringBuilder.append("<approvedate></approvedate>");
		stringBuilder.append("<lastadjustuser></lastadjustuser>");
		stringBuilder.append("<pk_busitype></pk_busitype>");
		stringBuilder.append("<money>"+money+"</money>");
		stringBuilder.append("<local_money>"+money+"</local_money>");
		stringBuilder.append("<billyear>"+billyear+"</billyear>");
		stringBuilder.append("<billperiod>"+billmonth+"</billperiod>");
		stringBuilder.append("<scomment></scomment>");
		stringBuilder.append("<effectstatus>"+effectstatus+"</effectstatus>");
		stringBuilder.append("<effectuser></effectuser>");
		stringBuilder.append("<effectdate></effectdate>");
		stringBuilder.append("<lastapproveid></lastapproveid>");
		stringBuilder.append("<def1></def1>");
		stringBuilder.append("<def30></def30>");
		stringBuilder.append("<def29></def29>");
		stringBuilder.append("<def28></def28>");
		stringBuilder.append("<def27></def27>");
		stringBuilder.append("<def26></def26>");
		stringBuilder.append("<def25></def25>");
		stringBuilder.append("<def24></def24>");
		stringBuilder.append("<def23></def23>");
		stringBuilder.append("<def22></def22>");
		stringBuilder.append("<def21></def21>");
		stringBuilder.append("<def20></def20>");
		stringBuilder.append("<def19></def19>");
		stringBuilder.append("<def18></def18>");
		stringBuilder.append("<def17></def17>");
		stringBuilder.append("<def16></def16>");
		stringBuilder.append("<def15></def15>");
		stringBuilder.append("<def14></def14>");
		stringBuilder.append("<def13></def13>");
		stringBuilder.append("<def12></def12>");
		stringBuilder.append("<def11></def11>");
		stringBuilder.append("<def10></def10>");
		stringBuilder.append("<def9></def9>");
		stringBuilder.append("<def8></def8>");
		stringBuilder.append("<def7></def7>");
		stringBuilder.append("<def6></def6>");
		stringBuilder.append("<def5></def5>");
		stringBuilder.append("<def4></def4>");
		stringBuilder.append("<def3></def3>");
		stringBuilder.append("<def2></def2>");		
		stringBuilder.append("<costcenter></costcenter>");
		stringBuilder.append("<creditorreference></creditorreference>");
		stringBuilder.append("<bodys>");
		
		logger.info("获取明细表数据");
        DetailTableInfo detailTableInfo = requestInfo.getDetailTableInfo();
        DetailTable[] detailTables = detailTableInfo.getDetailTable();
        logger.info("明细表数量:"+detailTables.length);



		//获取收款信息明细
		DetailTable receDetailTable=detailTables[1];
		//获取收款明细行
		Row[] receRows=receDetailTable.getRow();
		

			for (Row recerow : receRows) {
				Cell[] receCells=recerow.getCell();
				for (Cell rececell : receCells) {
					String recefield=rececell.getName();
					if(recefield.equals("bm")) {
						supplier=Util.null2String(rececell.getValue());
						logger.info("编码:"+supplier);
					}
					if(recefield.equals("zhxz")) {
						natureaccount=Util.null2String(rececell.getValue());
						logger.info("账户性质:"+natureaccount);
					}
					if(recefield.equals("skje")){
						skje=new BigDecimal(Util.null2String(rececell.getValue()));
						logger.info("收款金额:"+skje);
					}
					if (recefield.equals("skzh")) {
						recaccount=Util.null2String(rececell.getValue());
						logger.info("收款账户:"+recaccount);
					}
				}
				
				stringBuilder.append("<item>");
				stringBuilder.append("<sett_org>"+pk_org+"</sett_org>");
				stringBuilder.append("<pk_org>"+pk_org+"</pk_org>");
				stringBuilder.append("<pk_fiorg>"+pk_org+"</pk_fiorg>");
				stringBuilder.append("<pk_pcorg></pk_pcorg>");
				stringBuilder.append("<pu_org></pu_org>");
				stringBuilder.append("<pu_psndoc></pu_psndoc>");
				stringBuilder.append("<pu_deptid></pu_deptid>");
				stringBuilder.append("<prepay>0</prepay>");
				stringBuilder.append("<material></material>");
				if (natureaccount.equals("个人")) {
					stringBuilder.append("<supplier></supplier>");
				}else {
				    stringBuilder.append("<supplier>"+supplier+"</supplier>");
				}
				stringBuilder.append("<postunit></postunit>");
				stringBuilder.append("<postpricenotax>0.00000000</postpricenotax>");
				stringBuilder.append("<postquantity>0.00000000</postquantity>");
				stringBuilder.append("<postprice>0.00000000</postprice>");
				stringBuilder.append("<task></task>");
				stringBuilder.append("<checkdirection></checkdirection>");
				stringBuilder.append("<coordflag>0</coordflag>");
				stringBuilder.append("<equipmentcode></equipmentcode>");
				stringBuilder.append("<productline></productline>");
				stringBuilder.append("<cashitem></cashitem>");
				stringBuilder.append("<payflag>0</payflag>");
				stringBuilder.append("<bankrollprojet></bankrollprojet>");
				stringBuilder.append("<pausetransact>N</pausetransact>");
				stringBuilder.append("<billdate>"+nowdate+"</billdate>");
				stringBuilder.append("<payman></payman>");
				stringBuilder.append("<paydate></paydate>");
				stringBuilder.append("<pk_group>"+pk_group+"</pk_group>");
				stringBuilder.append("<pk_billtype>"+pk_billtype+"</pk_billtype>");
				stringBuilder.append("<billclass>"+billclass+"</billclass>");
				stringBuilder.append("<pk_tradetype>"+pk_tradetype+"</pk_tradetype>");
				stringBuilder.append("<pk_payitem></pk_payitem>");
				stringBuilder.append("<busidate>"+nowdate+"</busidate>");
				stringBuilder.append("<pk_paybill></pk_paybill>");
				stringBuilder.append("<pk_subjcode></pk_subjcode>");
				stringBuilder.append("<billno></billno>");
				if (natureaccount.equals("个人")) {
					stringBuilder.append("<objtype>3</objtype>");
				}
				if(natureaccount.equals("供应商")){
					stringBuilder.append("<objtype>1</objtype>");
				}
				stringBuilder.append("<rowno>0</rowno>");
				stringBuilder.append("<rowtype>0</rowtype>");
				stringBuilder.append("<direction>0</direction>");
				stringBuilder.append("<checktype></checktype>");
				stringBuilder.append("<pk_ssitem></pk_ssitem>");
				stringBuilder.append("<scomment>"+remark+"</scomment>");
				stringBuilder.append("<subjcode></subjcode>");
				stringBuilder.append("<pk_currtype>人民币</pk_currtype>");
				stringBuilder.append("<rate>0.00000000</rate>");
				stringBuilder.append("<pk_deptid>"+pk_deptid+"</pk_deptid>");
				if (natureaccount.equals("供应商")) {
					stringBuilder.append("<pk_psndoc></pk_psndoc>");
				}else {
				    stringBuilder.append("<pk_psndoc>"+supplier+"</pk_psndoc>");
				}
				stringBuilder.append("<money_de>"+skje+"</money_de>");
				stringBuilder.append("<local_money_de>"+skje+"</local_money_de>");
				stringBuilder.append("<money_bal>"+skje+"</money_bal>");
				stringBuilder.append("<local_money_bal>"+skje+"</local_money_bal>");
				stringBuilder.append("<quantity_bal>0.00000000</quantity_bal>");
				stringBuilder.append("<notax_de>"+skje+"</notax_de>");
				stringBuilder.append("<local_notax_de>"+skje+"</local_notax_de>");			
				stringBuilder.append("<price>0.00000000</price>");
				stringBuilder.append("<taxprice>0.00000000</taxprice>");
				stringBuilder.append("<pk_balatype>"+pk_balatype+"</pk_balatype>");
				stringBuilder.append("<top_billtype></top_billtype>");
				stringBuilder.append("<top_tradetype></top_tradetype>");
				stringBuilder.append("<src_tradetype></src_tradetype>");
				stringBuilder.append("<src_billtype> </src_billtype>");
				stringBuilder.append("<pk_payterm></pk_payterm>");
				stringBuilder.append("<top_termch></top_termch>");
				stringBuilder.append("<checkno></checkno>");
				stringBuilder.append("<payaccount>1102020409000161471</payaccount>");
				stringBuilder.append("<recaccount>"+recaccount+"</recaccount>");
				stringBuilder.append("<cashaccount></cashaccount>");
				stringBuilder.append("<ordercubasdoc></ordercubasdoc>");
				stringBuilder.append("<innerorderno></innerorderno>");
				stringBuilder.append("<assetpactno></assetpactno>");
				stringBuilder.append("<contractno></contractno>");
				stringBuilder.append("<freecust></freecust>");
				stringBuilder.append("<facard></facard>");
				stringBuilder.append("<purchaseorder></purchaseorder>");
				stringBuilder.append("<invoiceno></invoiceno>");
				stringBuilder.append("<outstoreno></outstoreno>");
				stringBuilder.append("<pk_jobphase></pk_jobphase>");
				stringBuilder.append("<pk_job></pk_job>");
				stringBuilder.append("<def30></def30>");
				stringBuilder.append("<def29></def29>");
				stringBuilder.append("<def28></def28>");
				stringBuilder.append("<def27></def27>");
				stringBuilder.append("<def26></def26>");
				stringBuilder.append("<def25></def25>");
				stringBuilder.append("<def24></def24>");
				stringBuilder.append("<def23></def23>");
				stringBuilder.append("<def22></def22>");
				stringBuilder.append("<def21></def21>");
				stringBuilder.append("<def20></def20>");
				stringBuilder.append("<def19></def19>");
				stringBuilder.append("<def18></def18>");
				stringBuilder.append("<def17></def17>");
				stringBuilder.append("<def16></def16>");
				stringBuilder.append("<def15></def15>");
				stringBuilder.append("<def14></def14>");
				stringBuilder.append("<def13></def13>");
				stringBuilder.append("<def12></def12>");
				stringBuilder.append("<def11></def11>");
				stringBuilder.append("<def10></def10>");
				stringBuilder.append("<def9></def9>");
				stringBuilder.append("<def8></def8>");
				stringBuilder.append("<def7></def7>");
				stringBuilder.append("<def6></def6>");
				stringBuilder.append("<def5></def5>");
				stringBuilder.append("<def4></def4>");
				stringBuilder.append("<def3></def3>");
				stringBuilder.append("<def2></def2>");
				stringBuilder.append("<def1></def1>");
				stringBuilder.append("<checkelement></checkelement>");
				stringBuilder.append("<grouprate>0.00000000</grouprate>");
				stringBuilder.append("<globalrate>0.00000000</globalrate>");
				stringBuilder.append("<groupdebit>0.00000000</groupdebit>");
				stringBuilder.append("<globaldebit>0.00000000</globaldebit>");
				stringBuilder.append("<groupbalance>0.00000000</groupbalance>");
				stringBuilder.append("<globalbalance>0.00000000</globalbalance>");
				stringBuilder.append("<groupnotax_de>0.00000000</groupnotax_de>");
				stringBuilder.append("<globalnotax_de>0.00000000</globalnotax_de>");
				stringBuilder.append("<isforce>0</isforce>");
				stringBuilder.append("<forcemoney>0.00000000</forcemoney>");
				stringBuilder.append("<forcestatus>0</forcestatus>");
				stringBuilder.append("<occupationmny>0.00000000</occupationmny>");
				stringBuilder.append("<bankrelated_code></bankrelated_code>");
				stringBuilder.append("<project></project>");
				stringBuilder.append("<project_task></project_task>");
				stringBuilder.append("<costcenter></costcenter>");
				stringBuilder.append("<confernum></confernum>");				
				stringBuilder.append("</item>");
			
			
		}
	
		stringBuilder.append("</bodys>");
		stringBuilder.append("<grouplocal>0.00000000</grouplocal>");
		stringBuilder.append("<globallocal>0.00000000</globallocal>");
		stringBuilder.append("<rate>0.00000000</rate>");
		stringBuilder.append("<grouprate>0.00000000</grouprate>");
		stringBuilder.append("<globalrate>0.00000000</globalrate>");
		stringBuilder.append("<checkelement></checkelement>");
		stringBuilder.append("<pu_deptid></pu_deptid>");
		stringBuilder.append("<pu_org></pu_org>");
		stringBuilder.append("<cashitem></cashitem>");
		stringBuilder.append("<bankrollprojet></bankrollprojet>");
		stringBuilder.append("<pk_deptid></pk_deptid>");
		stringBuilder.append("<pk_psndoc></pk_psndoc>");
		stringBuilder.append("<supplier></supplier>");
		stringBuilder.append("<pk_currtype></pk_currtype>");
		stringBuilder.append("<pk_subjcode></pk_subjcode>");
		stringBuilder.append("<pk_balatype></pk_balatype>");
		if (natureaccount.equals("个人")) {
			stringBuilder.append("<objtype>3</objtype>");
		}
		if(natureaccount.equals("供应商")){
			stringBuilder.append("<objtype>1</objtype>");
		}
		stringBuilder.append("<payaccount></payaccount>");
		stringBuilder.append("<recaccount></recaccount>");
		stringBuilder.append("<cashaccount></cashaccount>");
		stringBuilder.append("<coordflag>0</coordflag>");
		stringBuilder.append("<busidate>"+nowdate+"</busidate>");
		stringBuilder.append("</billhead>");
		stringBuilder.append("</bill>");
		stringBuilder.append("</ufinterface>");
		
		 ISendServiceToolsPortTypeProxy istptp = new ISendServiceToolsPortTypeProxy(webserviceurl);
		  try {
	        	logger.info(stringBuilder.toString());
				String result = istptp.sendDocument(backString, stringBuilder.toString());
				logger.info(result);
				List<String> list=getReturnInfo(result);
				if(!String.valueOf(list.get(0)).equals(RESULT_CODE)) {
					 requestInfo.getRequestManager().setMessageid(list.get(0));
		             requestInfo.getRequestManager().setMessagecontent(list.get(1));
		             logger.error(list.get(1));
		            return FAILURE_AND_CONTINUE;
				}
				
				String code=list.get(2);
				logger.info("NC应付单号:"+code);
				String requestid = requestInfo.getRequestid();
				String ncsql="update formtable_main_548 set ncpaycode="+"'"+code+"'"+" where requestid="+requestid;
				logger.info("更新应付单号sql语句:"+ncsql);
		        RecordSet recordSet=new RecordSet();
		        recordSet.executeUpdate(ncsql);
				
				return SUCCESS;
			} catch (Exception e) {
				 requestInfo.getRequestManager().setMessageid("525");
		         requestInfo.getRequestManager().setMessagecontent(e.getMessage());
				e.printStackTrace();
				return FAILURE_AND_CONTINUE;
			}
	
	}
	
	public List<String> getReturnInfo(String xmldata){
        Document document = null;
        List<String> list = null;
        try {
            document = DocumentHelper.parseText(xmldata);
            list = new ArrayList<String>();
            Element root = document.getRootElement();
            Element memberElm=root.element("sendresult");// "member"是节点名
            @SuppressWarnings("unchecked")
			Iterator<Element> ment = memberElm.elementIterator();
            while (ment.hasNext()) {
                Element m = ment.next();
                if (m.getName().equals("resultcode")){
                    list.add(m.getText());
                }
                if (m.getName().equals("resultdescription")){
                    list.add(m.getText());
                }
                if (m.getName().equals("content")){
                    list.add(m.getText());
                }
            }

        } catch (DocumentException e) {
            logger.error("错误信息："+e.getMessage(),e);
        }
        return list;
    }
	
}
