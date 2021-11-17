
public class CellSpreadSheet implements CellProvider{

        public static void main(String[] args)
        
         {
           
            
           
           

        }
            int columns;
            int rows;
            Cell[][] arraySheet;
    
            public CellSpreadSheet(int columns, int rows)
    {
        this.columns = columns;
        this.rows = rows;
        arraySheet = new Cell[columns][rows];
    }
    public Cell getCell(char column, int row)
    {
        /*if (arraySheet[column-65][row-1] == null)
        {
           return arraySheet[column-65][row-1];
        }*/

       return arraySheet[column-65][row-1];
    }
    //check each value to see if null or not
    public String getSpreadSheetAsCSV(boolean showFormulas)
    {
    //RAW
    if (showFormulas == true)
    {
        int o = 0;
        String returnedStr = "";
    //  char ascii = (char)x;
    //print headers
        for (char c = 'A'; c < columns + 65; c++)
        {
            o++;
            if (o  < columns)
            returnedStr = returnedStr + c +",";
            else
            {
                returnedStr = returnedStr + c;//","+c;
            }
           
        }
        returnedStr = returnedStr +  "\n" ;

    for (int rowInt = 0; rowInt < rows; rowInt++)
    {
    for (int  colInt= 0; colInt < columns; colInt++)
    {

    if (arraySheet[colInt][rowInt] == null)
    {
    returnedStr = returnedStr + "0.0";
    
    }
    else if (arraySheet[colInt][rowInt] instanceof FormulaCell)
    {
    returnedStr = returnedStr + arraySheet[colInt][rowInt].getStringValue();

    }
    else if (arraySheet[colInt][rowInt] instanceof DoubleCell)
    {
        returnedStr = returnedStr + arraySheet[colInt][rowInt].getNumericValue();
    }

    if (colInt + 1 < columns)
    returnedStr = returnedStr + ",";
    else
    {

    }
    }
    //System.out.println();
    returnedStr = returnedStr + "\n";
    }
    return returnedStr;


    }

    //evaluated
    else if (showFormulas == false)
    {

    int o = 0;
    String returnedStr = "";
    //  char ascii = (char)x;
    
    for (char c1 = 'A'; c1 < columns + 65; c1++)
    {
        o++;
        if (o  < columns)
        returnedStr = returnedStr + c1 +",";
        else
        {
           

            returnedStr = returnedStr + c1;//","+c;
        }
    }
    returnedStr = returnedStr +  "\n" ;
   
    for (int rowInt = 0; rowInt < rows; rowInt++)
    {
    for (int  colInt= 0; colInt < columns; colInt++)
    {

    if (arraySheet[colInt][rowInt] == null)
    {
    returnedStr = returnedStr + "0.0";
    }

     
    
     else if (arraySheet[colInt][rowInt] instanceof DoubleCell)
    {
    
    returnedStr = returnedStr +  arraySheet[colInt][rowInt].getNumericValue();
    }
    else if (arraySheet[colInt][rowInt] instanceof FormulaCell)
    {
    returnedStr = returnedStr + arraySheet[colInt][rowInt].getNumericValue();
   // System.out.println(returnedStr);

    }
    //}
    if (colInt + 1 < columns)
    returnedStr = returnedStr + ",";
    else
    {

    }
    }
    returnedStr = returnedStr + "\n";
    }
    return returnedStr;
    }
    return "";

    }



    public void setValue(char column, int row, String value)
    {
    //Expand Col(if necessary)
    if (column-65 >= columns )
    {
    expandColumnRange(column);
    }
    //Expand Rows(if necessary))
    /*if ( row < rows)
    {
    return;
    }
    */
    if (row > rows)
    {
    Cell[][] arraySheetExpandRow = new Cell[columns][row];

    for (int rowInt = 0;rowInt< rows; rowInt++)
    {
    for (int colInt = 0; colInt < columns; colInt++)
    {
    arraySheetExpandRow[colInt][rowInt] = arraySheet[colInt][rowInt];
    }
    }
/*  Service dining1 = new Service(10, 5, 80, "gives food");
    Service dining2 = new Service(10, 5, 80, "gives food");
    Set<Service> setOfServices = new HashSet<Service>();
    setOfServices.add(dining1);
    setOfServices.add(dining2);
    */
    this.rows = row;
    //copy old array to this (end at x) 

    arraySheet = arraySheetExpandRow;
    }
    boolean testDouble;
    try 
    { 
        Double.parseDouble(value); 
       testDouble = true;
    }  
    catch (NumberFormatException e)  
    { 
       testDouble = false;
    } 
if (testDouble == true)
{
//System.out.println("hi");
//CellSpreadSheet DoubleCell = new CellSpreadSheet(column-65, row-1);
//DoubleCell  = new arraySheet[column-65][row-1]; 
double doubVal = Double.parseDouble(value);
 arraySheet[column-65][row-1] = new DoubleCell(doubVal);
///arraySheet[column-65][row-1]
}
else if (testDouble == false)
{
arraySheet[column-65][row-1] = new FormulaCell(value, this);
}
    //arraySheet[column-65][row-1].getNumericValue();
    /*
if (arraySheet[column-65][row-1] instanceof DoubleCell)
{
    // double dub = arraySheet[column-65][row-1].getNumericValue();
     //System.out.println(dub);
  
    
}
else if (arraySheet[column-65][row-1] instanceof FormulaCell)
{

}


    else if (arraySheet[column-65][row-1] instanceof FormulaCell)
    {
        arraySheet[column-65][row-1].getStringValue();
        //arraySheet[column-65][row-1] = value;
    }
    */
    //double hi = Double.parseDouble(value);


    
    //  String hi = (String)arraySheet.getValue(column,row);
    // return value;
    }
    /*
    * Returns a complete copy of the spreadsheet data. Since it is a COPY, any edits to the copy
    will have no effect on the spreadsheet itself.
    * @return a complete copy of the spreadsheet data
    */
    public Cell[][] getCopyOfData()
    {
    //similar to Expand method just dont equal Arrays at the end
    Cell[][] arraySheetCopy = new Cell[columns][rows];

    for (int rowInt = 0;rowInt< rows; rowInt++)
    {
    for (int colInt = 0; colInt < columns; colInt++)
    {
    arraySheetCopy[colInt][rowInt] = arraySheet[colInt][rowInt];
    }
    }
    return arraySheetCopy;
    } 

    public void expandColumnRange(char column)
    {
    int i1 = 1;
    for (char c = 'A'; c < column; c++)
    {
        i1++;
    }
    if ( i1 < columns)
    {
    return;
    }

    Cell[][] arraySheetExpandCol = new Cell[i1][rows];

    for (int rowInt = 0;rowInt< rows; rowInt++)
    {
    for (int colInt = 0; colInt < columns; colInt++)
    {
    arraySheetExpandCol[colInt][rowInt] = arraySheet[colInt][rowInt];
    }
    }

    this.columns = i1;
    //copy old array to this (end at x) 

    arraySheet = arraySheetExpandCol;

    }

    public Cell[] getCopyOfColumnThroughRow(char c, int throughRow)
    {
    Cell [] arrayColumnThroughRow = new Cell[throughRow];
    for (int i = 0; i < throughRow && i < rows; i++)
    {
        arrayColumnThroughRow[i] = arraySheet[c-65][i];
    }

    return arrayColumnThroughRow;

    }

    public double getValue(char column,int row){
    //if parsedDouble then return (double)
    //else 
    int i = 0;
    char c;
    for ( c = 'A'; c <column; c++)
    {
    i++;
    }
    if ( column-65 >= columns)
    {
    return 0;
    }
    //String str = cell.substring(1,cell.length()); 
    //int j = Integer.parseInt(str);

    if (arraySheet[i][row-1] instanceof DoubleCell)
    {
        return arraySheet[i][row-1].getNumericValue();
   // double doubleValue = (double)arraySheet[i][row-1];  
    //return doubleValue;
    }
    else if (arraySheet[i][row-1] instanceof FormulaCell)
    {
    //  String StringValue = (String)arraySheet[i][j-1]; 
    return arraySheet[i][row-1].getNumericValue(); 
    //double dub = evaluateFormula(c, row);
    //return dub;
    }
    return 0;


    /*if (arraySheet[column][row] == null)
    return 0;

    else
    return 1;

    */
    }

    public double getValue(String cell){
    int i = 0;
    char c;
    for ( c = 'A'; c < cell.charAt(0); c++)
    {
    i++;
    }

    if ( i >= columns)
    {
    return 0;
    }

    String str = cell.substring(1,cell.length()); 
    int j = Integer.parseInt(str);


    if (arraySheet[i][j-1] instanceof DoubleCell)
    {
    //double doubleValue = (double)arraySheet[i][j-1];  
    return arraySheet[i][j-1].getNumericValue();
    }
    else if (arraySheet[i][j-1] instanceof FormulaCell)
    {
    //  String StringValue = (String)arraySheet[i][j-1];  
    //double dub = evaluateFormula(c, j);
    return arraySheet[i][j-1].getNumericValue();
    }
    return 0;
    }

    //cannot be invalid input here I think(must be talking about a formula)
    public double evaluateFormula(char column,int row)
    {
        double dub = getValue(column, row);
        return dub;
//return 0;
}
}
