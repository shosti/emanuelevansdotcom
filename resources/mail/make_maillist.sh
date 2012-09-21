#!/bin/bash

echo "Indexing..."
mu index --autoupgrade > /dev/null
SUBS_FILE="/tmp/subs.$$.tmp"
UNSUBS_FILE="/tmp/unsubs.$$.tmp"
mu find maildir:/Subscriptions -f f > $SUBS_FILE 2> /dev/null
mu find maildir:/Unsubscriptions -f f > $UNSUBS_FILE 2> /dev/null
grep -vf $UNSUBS_FILE $SUBS_FILE > $(dirname $0)/maillist
echo "Maillist created"
rm $SUBS_FILE
rm $UNSUBS_FILE
