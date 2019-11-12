package org.lionsoul.jcseg.analyzer;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.lionsoul.jcseg.DictionaryFactory;
import org.lionsoul.jcseg.ISegment;
import org.lionsoul.jcseg.JcsegTaskConfig;
import org.lionsoul.jcseg.dic.ADictionary;

/**
 * Jcseg tokenizer factory class for solr
 * 
 * @author chenxin<chenxin619315@gmail.com>
 */
public class JcsegTokenizerFactory extends TokenizerFactory 
{
    
    public final ISegment.Type type;
    public final JcsegTaskConfig config;
    public final ADictionary dic;

    /**
     * set the mode arguments in the schema.xml 
     *     configuration file to change the segment mode for Jcseg
     * @throws IOException 
     * 
     * @see TokenizerFactory#TokenizerFactory(Map)
     */
    public JcsegTokenizerFactory(Map<String, String> args) throws IOException
    {
        super(args);
        
        type = ISegment.Type.fromString(args.get("mode"));
        
        // initialize the task configuration and the dictionary
        config = new JcsegTaskConfig(true);
        // check and apply this-level Jcseg settings
        for ( Entry<String, String> entry : args.entrySet() ) {
        	if ( entry.getKey().startsWith("jcseg_") ) {
        		config.set(entry.getKey().replace("jcseg_", "jcseg."), entry.getValue());
        	}
        }
        
        dic = DictionaryFactory.createSingletonDictionary(config);
    }
    
    public JcsegTaskConfig getTaskConfig() 
    {
        return config;
    }
    
    public ADictionary getDict()
    {
        return dic;
    }

    @Override
    public Tokenizer create( AttributeFactory factory ) 
    {
        try {
            return new JcsegTokenizer(type, config, dic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
