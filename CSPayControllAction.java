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
 * @author sxq
 * 
 * @date  2019-09-05
    *   ����������(ͨ��)Ӧ����������
 *
 */
public class CSPayControllAction extends BaseAction  {
	private static Logger logger = LoggerFactory.getLogger(CSPayControllAction.class);


	private static final String pk_group="06";

	private static final String pk_org="220307000000";

	private static final String isreded="N";

	private static final String pk_billtype="F1";

	private static final String pk_tradetype="F1-Cxx-01";

	private static final String billclass="yf";

	private static final Integer accessorynum=0;

	private static final String isflowbill="N";

	private static final String isinit="N";

	private static final Integer syscode=0;

	private static final Integer src_syscode=0;

	private  static final Integer billstatus = 0;

	private static final String billmaker="OAFK";

	private static final Integer effectstatus=0;

	private static final String rececountryid="CN";

	private static final String taxcountryid="CN";
	
	private static final String RESULT_CODE="1";
	
	private static final String webserviceurl="http://10.10.2.5:9088/api/Domain/ydpubesb/sendWsTools?wsdl";
	private static final String backString="http://192.168.120.52:9081/service/XChangeServlet?account=0001&groupcode=06";
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
		String subjectcode="";
		String supplier="";
		String natureaccount="";
		String remark="";
		String subid="";
		//String sql="";
		String pk_deptid="";
		BigDecimal ratemoney=new BigDecimal(0);
		BigDecimal noratemoney=new BigDecimal(0);
		BigDecimal taxmoney=new BigDecimal(0);
		BigDecimal skje=new BigDecimal(0);
		BigDecimal money=new BigDecimal(0);
        //�տ���
		BigDecimal sumskje=new BigDecimal(0);
       //�������
		BigDecimal sumcxje=new BigDecimal(0);
        //ʵ�����
		//BigDecimal sumsfje=new BigDecimal(0);
		
		BigDecimal rate=new BigDecimal(0);
		String ncyfcode="";
		String taxnum="";
	    int taxrate=0;
	    int tax=0;
	    String recaccount="";
		StringBuilder stringBuilder=new StringBuilder();
		Property prop[]=requestInfo.getMainTableInfo().getProperty();
		for (int i = 0; i < prop.length; i++) {
			String field=prop[i].getName();
			if(field.equals("lcbh")) {
				billid=Util.null2String(prop[i].getValue());
				logger.info("���̱��:"+billid);
			}
			if(field.equals("sfje")) {
				money=new BigDecimal(Util.null2String(prop[i].getValue()));
				logger.info("ʵ�����:"+money);
				
			}
			//˰��ϼ�
			if(field.equals("bxje")) {
				sumskje=new BigDecimal(Util.null2String(prop[i].getValue()));
				logger.info("��˰���ϼ�:"+sumskje);
			}
			
			if (field.equals("tax")) {
				sumcxje=new BigDecimal(Util.null2String(prop[i].getValue()));
				logger.info("�������ϼ�:"+sumcxje);
			}
			if(field.equals("ncyfcode")) {
				ncyfcode=Util.null2String(prop[i].getValue());
			}
		}
		if(!ncyfcode.equals("")) {
			logger.info("������и���ţ��򲻴�NC");
			return SUCCESS;
		}
		
		
		
		stringBuilder.append("<?xml version=\"1.0\" encoding='GBK'?>");
		stringBuilder.append("<ufinterface account=\"develop\" billtype=\"F1\" businessunitcode=\"\" filename=\"\" groupcode=\"\" isexchange=\"Y\" orgcode=\"\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"LYSOA\">");
		stringBuilder.append("<bill id=\""+billid+"\">");
		logger.info("<bill id=\""+billid+"\">");
		stringBuilder.append("<billhead>");
		stringBuilder.append("<pk_group>"+pk_group+"</pk_group>");
		stringBuilder.append("<pk_org>"+pk_org+"</pk_org>");
		stringBuilder.append("<pk_pcorg></pk_pcorg>");
		stringBuilder.append("<pk_fiorg>"+pk_org+"</pk_fiorg>");
		stringBuilder.append("<sett_org>"+pk_org+"</sett_org>");
		stringBuilder.append("<isreded>"+isreded+"</isreded>");
		stringBuilder.append("<outbusitype></outbusitype>");
		stringBuilder.append("<officialprintuser></officialprintuser>");
		stringBuilder.append("<officialprintdate></officialprintdate>");
		stringBuilder.append("<modifiedtime></modifiedtime>");
		stringBuilder.append("<creationtime>"+nowdate+"</creationtime>");
		stringBuilder.append("<creator></creator>");
		stringBuilder.append("<pk_billtype>"+pk_billtype+"</pk_billtype>");
		stringBuilder.append("<pk_tradetype>"+pk_tradetype+"</pk_tradetype>");
		stringBuilder.append("<modifier></modifier>");
		stringBuilder.append("<billclass>"+billclass+"</billclass>");
		stringBuilder.append("<pk_payablebill></pk_payablebill>");
		stringBuilder.append("<accessorynum>"+accessorynum+"</accessorynum>");
		stringBuilder.append("<subjcode>"+subjectcode+"</subjcode>");
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
		stringBuilder.append("<def9>"+billid+"</def9>");
		stringBuilder.append("<costcenter></costcenter>");
		stringBuilder.append("<rececountryid>"+rececountryid+"</rececountryid>");
		stringBuilder.append("<taxcountryid>"+taxcountryid+"</taxcountryid>");
		stringBuilder.append("<bodys>");
		
		logger.info("��ȡ��ϸ��");
        DetailTableInfo detailTableInfo = requestInfo.getDetailTableInfo();
        DetailTable[] detailTables = detailTableInfo.getDetailTable();
        logger.info("��ȡ��ϸ������:"+detailTables.length);
   
		DetailTable costdetailTable=detailTables[0];
	    
		Row[] costRows=costdetailTable.getRow();
		
		DetailTable receDetailTable=detailTables[2];
	
		Row[] receRows=receDetailTable.getRow();
		for (Row costrow : costRows) {
		
			Cell[] costCells=costrow.getCell();
			for (Cell costcell : costCells) {
			    String costfield=costcell.getName();
			    if (costfield.equals("subject")) {
					subid=Util.null2String(costcell.getValue());
					String []sub=subid.split("_");
					subjectcode=sub[1];
					logger.info("���˿�Ŀ����:"+subjectcode);
				}
			    if (costfield.equals("abstract")) {
					remark=Util.null2String(costcell.getValue());
					logger.info("ժҪ:"+remark);
				}
			    if(costfield.equals("costdept")) {
			    	String deptid=Util.null2String(costcell.getValue());
			    	String[]dept=deptid.split("_");
			    	pk_deptid=dept[1];
			    	logger.info("���ò��ű���:"+pk_deptid);
			    }
			    if(costfield.equals("mx1sqje")) {
			    	ratemoney=new BigDecimal(Util.null2String(costcell.getValue()));
			    	logger.info("��˰���:"+ratemoney);
			    }
			    if (costfield.equals("mx1sqjenotax")) {
					noratemoney=new BigDecimal(Util.null2String(costcell.getValue()));
					logger.info("��˰���:"+noratemoney);
				}
			    if(costfield.equals("mx1tax")) {
			    	taxmoney=new BigDecimal(Util.null2String(costcell.getValue()));
			    	logger.info("˰��:"+taxmoney);
			    }
			    if(costfield.equals("taxrate")) {
			    	taxrate=new Integer(Util.null2String(costcell.getValue()));
			    	
			    	switch (taxrate) {
					case 0:
						tax=13;taxnum="CN05";rate=new BigDecimal(0.13);
						break;
					case 1:
						tax=9;taxnum="CN06";rate=new BigDecimal(0.9);
						break;
					case 2:
					    tax=6;taxnum="G03";rate=new BigDecimal(0.06);
					    break;
					case 3:
						tax=3;taxnum="G02";rate=new BigDecimal(0.03);
					    break;
					case 4:
						tax=0;taxnum="G01";rate=new BigDecimal(0.00);
						break;
					default:
						
						break;
					}
			    }
			   
			}
			 logger.info("˰����:"+taxrate);
			 logger.info("˰��:"+tax);
			 logger.info("˰��:"+taxnum);
			for (Row recerow : receRows) {
				//��ȡ�տ���ϸ��Ԫ����
				logger.info("��ȡ�տ���ϸ����");
				Cell[] receCells=recerow.getCell();
						
				for (Cell rececell : receCells) {
					String recefield=rececell.getName();
					if(recefield.equals("mxgysbm")) {
						supplier=Util.null2String(rececell.getValue());
						logger.info("����:"+supplier);
					}
					if(recefield.equals("yhszd")) {
						natureaccount=Util.null2String(rececell.getValue());
						logger.info("�˻�����:"+natureaccount);
					}
					if(recefield.equals("mx3skje")){
						skje=new BigDecimal(Util.null2String(rececell.getValue()));
						logger.info("�տ���:"+skje);
					}
					if (recefield.equals("mx3skzh")) {
						recaccount=Util.null2String(rececell.getValue());
						logger.info("�տ��˺�:"+recaccount);
					}
					
				}
				stringBuilder.append("<item>");
				stringBuilder.append("<pk_org>"+pk_org+"</pk_org>");
				stringBuilder.append("<pk_fiorg>"+pk_org+"</pk_fiorg>");
				stringBuilder.append("<pk_pcorg></pk_pcorg>");
				stringBuilder.append("<pu_deptid></pu_deptid>");
				stringBuilder.append("<pu_psndoc></pu_psndoc>");
				stringBuilder.append("<pu_org></pu_org>");
				stringBuilder.append("<sett_org>"+pk_org+"</sett_org>");
				stringBuilder.append("<material></material>");
				if (natureaccount.equals("����")) {
					stringBuilder.append("<supplier></supplier>");
				}else {
				    stringBuilder.append("<supplier>"+supplier+"</supplier>");
				}
				stringBuilder.append("<postunit></postunit>");
				stringBuilder.append("<postpricenotax>0.00000000</postpricenotax>");
				stringBuilder.append("<postquantity>0.00000000</postquantity>");
				stringBuilder.append("<postprice>0.00000000</postprice>");
				stringBuilder.append("<task></task>");
				stringBuilder.append("<coordflag>0</coordflag>");
				stringBuilder.append("<equipmentcode></equipmentcode>");
				stringBuilder.append("<productline></productline>");
				stringBuilder.append("<cashitem></cashitem>");
				stringBuilder.append("<bankrollprojet></bankrollprojet>");
				stringBuilder.append("<pausetransact>"+isinit+"</pausetransact>");
				stringBuilder.append("<billdate>"+nowdate+"</billdate>");
				stringBuilder.append("<pk_group>"+pk_group+"</pk_group>");
				stringBuilder.append("<pk_billtype>"+pk_billtype+"</pk_billtype>");
				stringBuilder.append("<billclass>"+billclass+"</billclass>");
				stringBuilder.append("<pk_tradetype>"+pk_tradetype+"</pk_tradetype>");
				stringBuilder.append("<pk_payablebill></pk_payablebill>");
				stringBuilder.append("<pk_payableitem></pk_payableitem>");
				stringBuilder.append("<busidate>"+nowdate+"</busidate>");
				stringBuilder.append("<pk_subjcode></pk_subjcode>");
				stringBuilder.append("<billno></billno>");
				if (natureaccount.equals("����")) {
					stringBuilder.append("<objtype>3</objtype>");
				}
				if(natureaccount.equals("��Ӧ��")){
					stringBuilder.append("<objtype>1</objtype>");
				}
				stringBuilder.append("<rowno>0</rowno>");
				stringBuilder.append("<rowtype>0</rowtype>");
				stringBuilder.append("<direction>0</direction>");
				stringBuilder.append("<checktype></checktype>");
				stringBuilder.append("<scomment>"+remark+"</scomment>");
				stringBuilder.append("<subjcode>"+subjectcode+"</subjcode>");
				stringBuilder.append("<pk_currtype>�����</pk_currtype>");
				stringBuilder.append("<rate>"+taxmoney+"</rate>");
				stringBuilder.append("<pk_deptid>"+pk_deptid+"</pk_deptid>");
				if (natureaccount.equals("��Ӧ��")) {
					stringBuilder.append("<pk_psndoc></pk_psndoc>");
				}else {
				    stringBuilder.append("<pk_psndoc>"+supplier+"</pk_psndoc>");
				}
				stringBuilder.append("<quantity_cr>0.00000000</quantity_cr>");
				if(costRows.length>=receRows.length) {
				    stringBuilder.append("<local_money_cr>"+ratemoney+"</local_money_cr>");
				    stringBuilder.append("<money_cr>"+ratemoney+"</money_cr>");
				    stringBuilder.append("<money_bal>"+ratemoney+"</money_bal>");
				    stringBuilder.append("<local_money_bal>"+ratemoney+"</local_money_bal>");
				    stringBuilder.append("<quantity_bal>0.00000000</quantity_bal>");
					stringBuilder.append("<local_tax_cr>"+taxmoney+"</local_tax_cr>");
					stringBuilder.append("<notax_cr>"+noratemoney+"</notax_cr>");
					stringBuilder.append("<local_notax_cr>"+noratemoney+"</local_notax_cr>");
					stringBuilder.append("<price>0.00000000</price>");
					stringBuilder.append("<taxprice>0.00000000</taxprice>");
					stringBuilder.append("<taxrate>"+tax+"</taxrate>");
				}else {
					stringBuilder.append("<local_money_cr>"+skje+"</local_money_cr>");
				    stringBuilder.append("<money_cr>"+skje+"</money_cr>");
				    stringBuilder.append("<money_bal>"+skje+"</money_bal>");
				    stringBuilder.append("<local_money_bal>"+skje+"</local_money_bal>");
				    stringBuilder.append("<quantity_bal>0.00000000</quantity_bal>");
				    taxmoney=skje.subtract(new BigDecimal(String.valueOf(CalTaxMoney(ratemoney, rate))));
				    noratemoney=CalTaxMoney(ratemoney, rate);
					stringBuilder.append("<local_tax_cr>"+taxmoney+"</local_tax_cr>");
					stringBuilder.append("<notax_cr>"+noratemoney+"</notax_cr>");
					stringBuilder.append("<local_notax_cr>"+noratemoney+"</local_notax_cr>");
					stringBuilder.append("<price>0.00000000</price>");
					stringBuilder.append("<taxprice>0.00000000</taxprice>");
					stringBuilder.append("<taxrate>"+tax+"</taxrate>");
				}
				
				stringBuilder.append("<top_billtype></top_billtype>");
				stringBuilder.append("<top_tradetype></top_tradetype>");
				stringBuilder.append("<src_tradetype></src_tradetype>");
				stringBuilder.append("<src_billtype></src_billtype>");
				stringBuilder.append("<taxtype>0</taxtype>");
				stringBuilder.append("<pk_payterm></pk_payterm>");
				stringBuilder.append("<payaccount></payaccount>");
				stringBuilder.append("<recaccount></recaccount>");
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
				stringBuilder.append("<groupcrebit>0.00000000</groupcrebit>");
				stringBuilder.append("<globalcrebit>0.00000000</globalcrebit>");
				stringBuilder.append("<groupbalance>0.00000000</groupbalance>");
				stringBuilder.append("<globalbalance>0.00000000</globalbalance>");
				stringBuilder.append("<groupnotax_cre>0.00000000</groupnotax_cre>");
				stringBuilder.append("<globalnotax_cre>0.00000000</globalnotax_cre>");
				stringBuilder.append("<occupationmny>0.00000000</occupationmny>");
				stringBuilder.append("<project></project>");
				stringBuilder.append("<project_task></project_task>");
				stringBuilder.append("<settleno></settleno>");
				stringBuilder.append("<costcenter></costcenter>");
				stringBuilder.append("<confernum></confernum>");
				stringBuilder.append("<sendcountryid></sendcountryid>");
				stringBuilder.append("<buysellflag>2</buysellflag>");
				stringBuilder.append("<buysellflag>"+taxnum+"</buysellflag>");
				stringBuilder.append("<nosubtaxrate></nosubtaxrate>");
				stringBuilder.append("<nosubtax></nosubtax>");
				stringBuilder.append("<caltaxmny></caltaxmny>");
				stringBuilder.append("<opptaxflag></opptaxflag>");
				stringBuilder.append("<vendorvatcode></vendorvatcode>");
				stringBuilder.append("<vatcode></vatcode>");
				stringBuilder.append("</item>");
				
			}
			
		}
		
		stringBuilder.append("</bodys>");
		stringBuilder.append("<grouplocal>0.00000000</grouplocal>");
		stringBuilder.append("<globallocal>0.00000000</globallocal>");
		stringBuilder.append("<grouprate>0.00000000</grouprate>");
		stringBuilder.append("<globalrate>0.00000000</globalrate>");
		stringBuilder.append("<checkelement></checkelement>");
		stringBuilder.append("<rate>0.00000000</rate>");
		stringBuilder.append("<pu_deptid></pu_deptid>");
		stringBuilder.append("<pu_psndoc></pu_psndoc>");
		stringBuilder.append("<pu_org></pu_org>");
		stringBuilder.append("<cashitem></cashitem>");
		stringBuilder.append("<bankrollprojet></bankrollprojet>");
		stringBuilder.append("<pk_deptid></pk_deptid>");
		stringBuilder.append("<pk_psndoc></pk_psndoc>");
		stringBuilder.append("<supplier></supplier>");
		stringBuilder.append("<pk_currtype>�����</pk_currtype>");
		stringBuilder.append("<payaccount></payaccount>");
		stringBuilder.append("<recaccount></recaccount>");
		stringBuilder.append("<pk_balatype></pk_balatype>");
		
		if (natureaccount.equals("����")) {
			stringBuilder.append("<objtype>3</objtype>");
		}
		if(natureaccount.equals("��Ӧ��")){
			stringBuilder.append("<objtype>1</objtype>");
		}
		stringBuilder.append("<ispuadjust>N</ispuadjust>");
		stringBuilder.append("<coordflag></coordflag>");
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
					 requestInfo.getRequestManager().setMessageid((String)list.get(0));
		             requestInfo.getRequestManager().setMessagecontent((String)list.get(1));
		             logger.error((String)list.get(1));
		            return FAILURE_AND_CONTINUE;
				}
				String code=(String) list.get(2);
				logger.info("NCӦ������:"+code);
				String requestid = requestInfo.getRequestid();
				String ncsql="update formtable_main_600 set ncyfcode="+"'"+code+"'"+" where requestid="+requestid;
				logger.info("����Ӧ������sql���:"+ncsql);
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
	            Element memberElm=root.element("sendresult");// "member"�ǽڵ���
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
	            logger.error("������Ϣ��"+e.getMessage(),e);
	        }
	        return list;
	    }
	 
	 
	 private BigDecimal CalTaxMoney(BigDecimal notax,BigDecimal tax) {
		 BigDecimal basenum=new BigDecimal("1");
		return  notax.divide(basenum.add(tax),2,BigDecimal.ROUND_HALF_UP);
		 
	 }

}

