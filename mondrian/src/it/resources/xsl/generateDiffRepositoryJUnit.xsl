<?xml version="1.0"?> 
<!--
  == This software is subject to the terms of the Eclipse Public License v1.0
  == Agreement, available at the following URL:
  == http://www.eclipse.org/legal/epl-v10.html.
  == You must accept the terms of that agreement to use this software.
  ==
  == Copyright (C) 2007-2009 Pentaho and others
  == All Rights Reserved.
  -->
<!-- This stylesheet takes as input a DiffRepository .ref.xml file -->
<!-- containing Foodmart query/result pairs -->
<!-- and generates a JUnit class for running them from within an IDE. -->
<xsl:stylesheet 
  version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  >
  <xsl:output method="text" indent="no" />

  <xsl:param name="reffile"/>
  <xsl:param name="packagename"/>
  <xsl:param name="classname"/>

  <xsl:template match="Root">
// DO NOT EDIT THIS FILE OR CHECK IT INTO PERFORCE!
// This class was generated by ant target generateDiffRepositoryJUnit
// from <xsl:value-of select="$reffile"/>
package <xsl:value-of select="$packagename"/>;

import mondrian.test.*;
import mondrian.olap.*;
import mondrian.rolap.BatchTestCase;

/**
 * Wraps DiffRepository-based tests with JUnit test methods so
 * that you can see the query and result directly from within your 
 * favorite JUnit-aware IDE.
 */
public class <xsl:value-of select="$classname"/> extends BatchTestCase
{
  
    public <xsl:value-of select="$classname"/>(String testName)
        throws Exception
    {
        super(testName);
    }
    <xsl:apply-templates select="node()" />
    
}
  </xsl:template>

  <xsl:template name="genTxt">
    <xsl:param name="text" select="."/>
    <xsl:param name="start" select="substring(normalize-space($text),1,1)"/>
    <xsl:param name="ltrim" 
      select="concat($start,substring-after($text,$start))"/>
    <xsl:param name="escape">
      <xsl:call-template name="replaceStrings">
        <xsl:with-param name="text" select="$ltrim"/>
        <xsl:with-param name="replace" select="'&quot;'"/>
        <xsl:with-param name="with" select="'\&quot;'"/>
      </xsl:call-template>
    </xsl:param>
    <xsl:call-template name="replaceStrings">
      <xsl:with-param name="text" select="$escape"/>
      <xsl:with-param name="replace" select="'&#xa;'"/>
      <xsl:with-param name="with" select="'\n&quot; +&#xa;&quot;'"/>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="replaceStrings">
    <xsl:param name="text"/>
    <xsl:param name="replace"/>
    <xsl:param name="with"/>
    <xsl:choose>
      <xsl:when test="normalize-space($text)=''"> 
      </xsl:when> 
      <xsl:when test="contains($text, $replace)">
        <xsl:value-of select="substring-before($text, $replace)"/>
        <xsl:value-of select="$with"/>
        <xsl:call-template name="replaceStrings">
          <xsl:with-param name="text" select="substring-after($text, $replace)"/>
          <xsl:with-param name="replace" select="$replace"/>
          <xsl:with-param name="with" select="$with"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="TestCase">
      <xsl:param name="modifiedCubeName" select="Resource[@name='modifiedCubeName']"/>
      <xsl:param name="customDimensions" select="Resource[@name='customDimensions']"/>
      <xsl:param name="calculatedMembers" select="Resource[@name='calculatedMembers']"/>
      <xsl:param name="namedSets" select="Resource[@name='namedSets']"/>
    public void <xsl:value-of select="@name"/>()
        throws Exception
    { 
      TestContext testContext = getTestContext();  
      <xsl:if test="not(normalize-space(string($modifiedCubeName)) = '')" >
        testContext =   testContext.createSubstitutingCube(
            &quot;<xsl:value-of select="normalize-space(Resource[@name='modifiedCubeName'])"/>&quot;,
            <xsl:choose>
              <xsl:when
                test="normalize-space(string($customDimensions)) = ''">
            null,
              </xsl:when>
              <xsl:otherwise>
            &quot;<xsl:call-template name="genTxt">
              <xsl:with-param name="text" select="Resource[@name='customDimensions']"/>
              </xsl:call-template>&quot;,
              </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
              <xsl:when
                test="normalize-space(string($calculatedMembers)) = ''">
            null,
              </xsl:when>
              <xsl:otherwise>
            &quot;<xsl:call-template name="genTxt">
              <xsl:with-param name="text" select="Resource[@name='calculatedMembers']"/>
              </xsl:call-template>&quot;,
              </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
              <xsl:when
                test="normalize-space(string($namedSets)) = ''">
            null
              </xsl:when>
              <xsl:otherwise>
            &quot;<xsl:call-template name="genTxt">
              <xsl:with-param name="text" select="Resource[@name='namedSets']"/>
              </xsl:call-template>&quot;,
              </xsl:otherwise>
            </xsl:choose>);
      </xsl:if>
        testContext.assertQueryReturns(
&quot;<xsl:call-template name="genTxt">
      <xsl:with-param name="text" select="Resource[@name='mdx']"/>
      </xsl:call-template>&quot;, 
            fold(
&quot;<xsl:call-template name="genTxt">
      <xsl:with-param name="text" select="Resource[@name='result']"/>
      </xsl:call-template>&quot;));
    }
  </xsl:template>
</xsl:stylesheet>
