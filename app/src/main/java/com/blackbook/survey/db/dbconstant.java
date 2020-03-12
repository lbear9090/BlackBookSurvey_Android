package com.blackbook.survey.db;

public class dbconstant
{
    //table and feild names
	
	//surveytype table
	public static String Table_SurveyType ="tblsurveytype";
	
    public static String Stid ="surveytypeid";
    public static String Stname ="surveytypename";
    public static String Stlevel ="level";
    public static String Stparntid ="parentid";
    public static String Stcreateddate ="createddate";
    public static String Stmodifieddate ="modifieddate";
    public static String Stisdeleted ="isdeleted";


    //Vendors table
    public static String Table_Vendors ="tblvendors";

    public static String Vid ="vendorid";
    public static String Vname ="vendorname";
    public static String Vcreateddate ="createddate";
    public static String Vmodifieddate ="modifieddate";
    public static String Visdeleted ="isdeleted";

    //OrganizationType table
    public static String Table_OrganizationType ="tblorganizationtype";

    public static String Otid ="organizationtypeid";
    public static String Otame ="organizationtypename";
    public static String Otlevel ="level";
    public static String Otparentid ="parentid";
    public static String Otisoptional ="isoptional";
    public static String Otcreateddate ="createddate";
    public static String Otmodifieddate ="modifieddate";
    public static String Otisdeleted ="isdeleted";

    //Rate table
    public static String Table_Rates ="tblrates";

    public static String Rateid ="rateid";
    public static String Ratename ="ratename";
    public static String Ratecreateddate ="createddate";
    public static String Ratemodifieddate ="modifieddate";
    public static String Rateisdeleted ="isdeleted";


    //Roles table
    public static String Table_Roles ="tblroles";

    public static String Rid ="roleid";
    public static String Rname ="rolename";
    public static String Rlevel ="level";
    public static String Rparentid ="parentid";
    public static String Risoptional ="isoptional";
    public static String Rcreateddate ="createddate";
    public static String Rmodifieddate ="modifieddate";
    public static String Risdeleted ="isdeleted";


    //Question table
    public static String Table_Question ="tblquestion";

    public static String Qid ="questionid";
    public static String Qtitle ="questiontitle";
    public static String Qdescription ="questiondescription";
    public static String Qtype ="questiontype";
    public static String Qformat ="questionformat";
    public static String Qoptioncount="optioncount";
    public static String Qcreateddate ="createddate";
    public static String Qmodifieddate ="modifieddate";
    public static String Qisdeleted ="isdeleted";

    //ScoreMatrix table
    public static String Table_ScoreMatrix ="tblscorematrix";

    public static String Smid ="matrixid";
    public static String Smtitle ="matrixtitle";
    public static String Smdescription ="matrixdescription";
    public static String Smstartrange ="startrange";
    public static String Smendrange ="endrange";
    public static String Smcreateddate ="createddate";
    public static String Smmodifieddate ="modifieddate";
    public static String Smisdeleted ="isdeleted";

    //Prefrences table
    public static String Table_Prefrences ="tblprefrences";

    public static String Pid ="prefrenceid";
    public static String Ptext ="preferencetext";
    public static String Pcreateddate ="createddate";
    public static String Pmodifieddate ="modifieddate";
    public static String Pisdeleted ="isdeleted";

    //CountryCode table
    public static String Table_Countrycode ="tblcountrycode";

    public static String Ccid ="countryid";
    public static String Cciso2 ="iso2";
    public static String Ccshortname ="shortname";
    public static String Cclongname ="longname";
    public static String Cciso3 ="iso3";
    public static String Cccallingcode ="callingcode";
}
