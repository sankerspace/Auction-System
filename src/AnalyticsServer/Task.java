/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalyticsServer;

import RMI.ManagementClientCallBackInterface;

/**
 *
 * @author sanker
 */
 public class Task{
        
       
            private SUBSCRIBER   subscriber=null;
            private UNSUBSCRIBER unsubscriber=null;
            private REMOTEERROR  remoteerror=null;
            private CLOSE        close=null;
            private boolean isSubscriber;
            private boolean isUnsubscriber;
            private boolean isRemoteError;
            private boolean isClose;
        
        public Task(SUBSCRIBER   subscriber)
        {
            isSubscriber=true;
            isUnsubscriber=false;
            isRemoteError=false;
            isClose=false;
            this.subscriber=subscriber;
        }
        
        public Task(UNSUBSCRIBER   unsubscriber)
        {
           isSubscriber=false;
           isUnsubscriber=true;
           isRemoteError=false;
           isClose=false;
           this.unsubscriber=unsubscriber; 
        }
        
        public Task(REMOTEERROR   error)
        {
            isSubscriber=false;
            isUnsubscriber=false;
            isRemoteError=true;
            isClose=false;
            this.remoteerror=error;
           
        }
        
        public Task(CLOSE close)
        {
            isSubscriber=false;
            isUnsubscriber=false;
            isRemoteError=false;
            isClose=true;
            this.close=close;
           
        }
        
        
        public boolean isSubscriber()
        {
            return this.isSubscriber;
        }
        
        public boolean isUnsubscriber()
        {
            return this.isUnsubscriber;
        }
        
        public boolean isRemoteError()
        {
            return this.isRemoteError;
        }
        
        public boolean isClose()
        {
            return this.isClose;
        }
        
        public SUBSCRIBER getSubscriber()
        {
            if(isSubscriber)
                return this.subscriber;
            else return null;
        }
        
        public UNSUBSCRIBER getUnSubscriber()
        {
            if(isUnsubscriber)
                return this.unsubscriber;
            else return null;
        }
        public REMOTEERROR getRemoteError()
        {
            if(isRemoteError)
                return this.remoteerror;
            else
                return null;
        }
        
    
        public static class SUBSCRIBER
        {
            public ManagementClientCallBackInterface mccbi; 
            public long subscribeID;
            public String regex;
             
            public SUBSCRIBER(ManagementClientCallBackInterface mccbi,long subscribeID,String regex)
            {
                this.mccbi=mccbi;
                this.regex=regex;
                this.subscribeID=subscribeID;
            }
             
        
        }
        
        public static class UNSUBSCRIBER
        {
            
            public long subscriptionID;
            
            public UNSUBSCRIBER(long subscriptionID)
            {
                this.subscriptionID=subscriptionID;
            }
        
        }
        
        public static class RESULT
        {
        
            public boolean success;
            public long subscriptionID;
            public RESULT(boolean success,long subscriptionID)
            {
                this.success=success;
                this.subscriptionID=subscriptionID;
            }
        }
        
        public static class REMOTEERROR
        {
            public long ClientID;
            public Filter filter=null;
            public ManagementClientCallBackInterface mccbi=null;
            public REMOTEERROR(long ClientID,
                        Filter filter,
                        ManagementClientCallBackInterface mccbi)
            {
                this.ClientID=ClientID;
                this.filter=filter;
                this.mccbi=mccbi;
                
            }
    
        }
        
        public static class CLOSE
        {
           //only a marker 
        }
    
    }