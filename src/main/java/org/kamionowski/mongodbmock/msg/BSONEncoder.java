/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.msg;

/**
 *
 * @author soldier
 */

import com.mongodb.DBRefBase;
import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.OutputBuffer;
import org.bson.types.*;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.bson.BSON.*;

/**
 * this is meant to be pooled or cached
 * there is some per instance memory for string conversion, etc...
 */
@SuppressWarnings("unchecked")
public class BSONEncoder {
    
    static final boolean DEBUG = false;

    public BSONEncoder(){

    }

    public byte[] encode( BSONObject o ){
        BasicOutputBuffer buf = new BasicOutputBuffer();
        set( buf );
        putObject( o );
        done();
        return buf.toByteArray();
    }

    public void set( OutputBuffer out ){
        if ( buffer != null )
            throw new IllegalStateException( "in the middle of something" );
        
        //buffer = out;
    }
 
    public void done(){
        buffer = null;
    }
   
    /**
     * @return true if object was handled
     */
    protected boolean handleSpecialObjects( String name , BSONObject o ){
        return false;
    }
    
    protected boolean putSpecial( String name , Object o ){
        return false;
    }

    /** Encodes a <code>BSONObject</code>.
     * This is for the higher level api calls
     * @param o the object to encode
     * @return the number of characters in the encoding
     */
    public int putObject( BSONObject o ){
        return putObject( null , o );
    }

    /**
     * this is really for embedded objects
     */
    int putObject( String name , BSONObject o ){

        if ( o == null )
            throw new NullPointerException( "can't save a null object" );

        if ( DEBUG ) System.out.println( "putObject : " + name + " [" + o.getClass() + "]" + " # keys " + o.keySet().size() );
        
        final int start = buffer.position();
        
        byte myType = OBJECT;
        if ( o instanceof List )
            myType = ARRAY;

        if ( handleSpecialObjects( name , o ) )
            return buffer.position() - start;
        
        if ( name != null ){
            _put( myType , name );
        }

        final int sizePos = buffer.position();
        buffer.putInt( 0 ); // leaving space for this.  set it at the end

        List transientFields = null;
        boolean rewriteID = myType == OBJECT && name == null;
        

        if ( myType == OBJECT ) {
            if ( rewriteID && o.containsField( "_id" ) )
                _putObjectField( "_id" , o.get( "_id" ) );
            
            {
                Object temp = o.get( "_transientFields" );
                if ( temp instanceof List )
                    transientFields = (List)temp;
            }
        }
        
        //TODO: reduce repeated code below.
        if ( o instanceof Map ){
	        for ( Entry<String, Object> e : ((Map<String, Object>)o).entrySet() ){
	        	
	            if ( rewriteID && e.getKey().equals( "_id" ) )
	                continue;
	            
	            if ( transientFields != null && transientFields.contains( e.getKey() ) )
	                continue;
	            
	            _putObjectField( e.getKey() , e.getValue() );
	
	        }        	
        } else {
	        for ( String s : o.keySet() ){
	
	            if ( rewriteID && s.equals( "_id" ) )
	                continue;
	            
	            if ( transientFields != null && transientFields.contains( s ) )
	                continue;
	            
	            Object val = o.get( s );
	
	            _putObjectField( s , val );
	
	        }
        }
        buffer.put( EOO );
        
        buffer.putInt( sizePos , buffer.position() - sizePos );
        return buffer.position() - start;
    }

	protected void _putObjectField( String name , Object val ){

        if ( name.equals( "_transientFields" ) )
            return;
        
        if ( DEBUG ) System.out.println( "\t put thing : " + name );
        
        if ( name.equals( "$where") && val instanceof String ){
            _put( CODE , name );
            _putValueString( val.toString() );
            return;
        }
        
        val = BSON.applyEncodingHooks( val );

        if ( val == null )
            putNull(name);
        else if ( val instanceof Date )
            putDate( name , (Date)val );
        else if ( val instanceof Number )
            putNumber(name, (Number)val );
        else if ( val instanceof String )
            putString(name, val.toString() );
        else if ( val instanceof ObjectId )
            putObjectId(name, (ObjectId)val );
        else if ( val instanceof BSONObject )
            putObject(name, (BSONObject)val );
        else if ( val instanceof Boolean )
            putBoolean(name, (Boolean)val );
        else if ( val instanceof Pattern )
            putPattern(name, (Pattern)val );
        else if ( val instanceof Map )
            putMap( name , (Map)val );
        else if ( val instanceof Iterable)
            putIterable( name , (Iterable)val );
        else if ( val instanceof byte[] )
            putBinary( name , (byte[])val );
        else if ( val instanceof Binary )
            putBinary( name , (Binary)val );
        else if ( val instanceof UUID )
            putUUID( name , (UUID)val );
        else if ( val.getClass().isArray() )
        	putArray( name , val );

        else if (val instanceof Symbol) {
            putSymbol(name, (Symbol) val);
        }
        else if (val instanceof BSONTimestamp) {
            putTimestamp( name , (BSONTimestamp)val );
        }
        else if (val instanceof CodeWScope) {
            putCodeWScope( name , (CodeWScope)val );
        }
        else if (val instanceof Code) {
            putCode( name , (Code)val );
        }
        else if (val instanceof DBRefBase) {
            BSONObject temp = new BasicBSONObject();
            temp.put("$ref", ((DBRefBase)val).getRef());
            temp.put("$id", ((DBRefBase)val).getId());
            putObject( name, temp );
        }
        else if ( putSpecial( name , val ) ){
            // no-op
        }
        else {
            throw new IllegalArgumentException( "can't serialize " + val.getClass() );
        }
        
    }
	
    private void putArray( String name , Object array ) {
        _put( ARRAY , name );
        final int sizePos = buffer.position();
        buffer.putInt( 0 );
                	        
        int size = Array.getLength(array);
        for ( int i = 0; i < size; i++ )
            _putObjectField( String.valueOf( i ) , Array.get( array, i ) );

        buffer.put( EOO );
        buffer.putInt( sizePos , buffer.position() - sizePos ); 
    }
	
    private void putIterable( String name , Iterable l ){
        _put( ARRAY , name );
        final int sizePos = buffer.position();
        buffer.putInt( 0 );
        
        int i=0;
        for ( Object obj: l ) {
            _putObjectField( String.valueOf( i ) , obj );
            i++;
        }
        	

        buffer.put( EOO );
        buffer.putInt( sizePos , buffer.position() - sizePos );        
    }
    
    private void putMap( String name , Map m ){
        _put( OBJECT , name );
        final int sizePos = buffer.position();
        buffer.putInt( 0 );
        
        for ( Map.Entry entry : (Set<Map.Entry>)m.entrySet() )
            _putObjectField( entry.getKey().toString() , entry.getValue() );

        buffer.put( EOO );
        buffer.putInt( sizePos , buffer.position() - sizePos );
    }
    

    protected void putNull( String name ){
        _put( NULL , name );
    }

    protected void putUndefined(String name){
        _put(UNDEFINED, name);
    }

    protected void putTimestamp(String name, BSONTimestamp ts ){
        _put( TIMESTAMP , name );
        buffer.putInt( ts.getInc() );
        buffer.putInt( ts.getTime() );
    }
    
    protected void putCodeWScope( String name , CodeWScope code ){
        _put( CODE_W_SCOPE , name );
        int temp = buffer.position();
        buffer.putInt( 0 );
        _putValueString( code.getCode() );
        putObject( code.getScope() );
        buffer.putInt( temp , buffer.position() - temp );
    }

    protected void putCode( String name , Code code ){
        _put( CODE , name );
        int temp = buffer.position();
        _putValueString( code.getCode() );
    }

    protected void putBoolean( String name , Boolean b ){
        _put( BOOLEAN , name );
        buffer.put( b ? (byte)0x1 : (byte)0x0 );
    }

    protected void putDate( String name , Date d ){
        _put( DATE , name );
        buffer.putLong( d.getTime() );
    }

    protected void putNumber( String name , Number n ){
		if ( n instanceof Integer ||
	             n instanceof Short ||
	             n instanceof Byte ||
	             n instanceof AtomicInteger ){
		    _put( NUMBER_INT , name );
		    buffer.putInt( n.intValue() );
		}
	    else if ( n instanceof Long || n instanceof AtomicLong ) {
	        _put( NUMBER_LONG , name );
	        buffer.putLong( n.longValue() );
	    }
	    else if ( n instanceof Float || n instanceof Double ) {
	      _put( NUMBER , name );
	      buffer.putDouble( n.doubleValue() );
	    }
		else {
	        throw new IllegalArgumentException( "can't serialize " + n.getClass() );
		}
    }
    
    protected void putBinary( String name , byte[] data ){
        _put( BINARY , name );
        buffer.putInt( 4 + data.length );

        buffer.put( B_BINARY );
        buffer.putInt( data.length );
        int before = buffer.position();
        buffer.put( data );
        int after = buffer.position();
        
        com.mongodb.util.MyAsserts.assertEquals( after - before , data.length );
    }

    protected void putBinary( String name , Binary val ){
        _put( BINARY , name );
        buffer.putInt( val.length() );
        buffer.put( val.getType() );
        buffer.put( val.getData() );
    }
    
    protected void putUUID( String name , UUID val ){
        _put( BINARY , name );
        buffer.putInt( 16 );
        buffer.put( B_UUID );
        buffer.putLong( val.getMostSignificantBits());
        buffer.putLong( val.getLeastSignificantBits());
    }

    protected void putSymbol( String name , Symbol s ){
        _putString(name, s.getSymbol(), SYMBOL);
    }

    protected void putString(String name, String s) {
        _putString(name, s, STRING);
    }

    private void _putString( String name , String s, byte type ){
        _put( type , name );
        _putValueString( s );
    }

    protected void putObjectId( String name , ObjectId oid ){
        _put( OID , name );
        // according to spec, values should be stored big endian
        //buffer.putIntBE( oid._time() );
        //buffer.putIntBE( oid._machine() );
        //buffer.putIntBE( oid._inc() );
    }
    
    private void putPattern( String name, Pattern p ) {
        _put( REGEX , name );
        _put( p.pattern() );
        _put( regexFlags( p.flags() ) );
    }


    // ----------------------------------------------
    
    /**
     * Encodes the type and key.
     * 
     */
    protected void _put( byte type , String name ){
        buffer.put( type );
        _put( name );
    }

    protected void _putValueString( String s ){
        int lenPos = buffer.position();
        buffer.putInt( 0 ); // making space for size
        int strLen = _put( s );
        buffer.putInt( lenPos , strLen );
    }
    
    void _reset( Buffer b ){
        b.position(0);
        b.limit( b.capacity() );
    }

    /**
     * puts as utf-8 string
     */
    protected int _put( String str ){

        final int len = str.length();
        int total = 0;

        for ( int i=0; i<len; ){
            int c = Character.codePointAt( str , i );

            if ( c < 0x80 ){
                buffer.put( (byte)c );
                total += 1;
            }
            else if ( c < 0x800 ){
                buffer.put( (byte)(0xc0 + (c >> 6) ) );
                buffer.put( (byte)(0x80 + (c & 0x3f) ) );
                total += 2;
            }
            else if (c < 0x10000){
                buffer.put( (byte)(0xe0 + (c >> 12) ) );
                buffer.put( (byte)(0x80 + ((c >> 6) & 0x3f) ) );
                buffer.put( (byte)(0x80 + (c & 0x3f) ) );
                total += 3;
            }
            else {
                buffer.put( (byte)(0xf0 + (c >> 18)) );
                buffer.put( (byte)(0x80 + ((c >> 12) & 0x3f)) );
                buffer.put( (byte)(0x80 + ((c >> 6) & 0x3f)) );
                buffer.put( (byte)(0x80 + (c & 0x3f)) );
                total += 4;
            }
            
            i += Character.charCount(c);
        }  
        
        buffer.put( (byte)0 );
        total++;
        return total;
    }

    public void putInt( int x ){
        buffer.putInt( x );
    }

    public void putLong( long x ){
        buffer.putLong( x );
    }
    
    public void putCString( String s ){
        _put( s );
    }

    protected IoBuffer buffer;

}

