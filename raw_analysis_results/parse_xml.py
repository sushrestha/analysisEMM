

# python code to count the potential issue title along with severity

import os ,sys
from xml.dom.minidom import parse


def main(xml):
	tags = ['Severity', 'Title']
	tags_count = []
	print 'the tags checked are: ',tags
	# parse the xml
	dom = parse(xml)
	# fitler the tag
	# tag = 'Title'
	# node = dom.getElementsByTagName(tag)
	# for tag in tags:
	# 	print tag
	# 	print tags_count
	# 	tags_count.append(dom.getElementsByTagName(tag))		
	# 	print tags_count
	# print 'final_tag:', tags_count

	# for tag_count in tags_count:
	# 	print tag_count[tags_count.index(tag_count)][0].firstChild.nodeValue

	tag1 = dom.getElementsByTagName(tags[0])
	tag2 = dom.getElementsByTagName(tags[1])

	# print tag1,len(tag1)
	# print tag2,len(tag1)


	counter = {}

	if len(tag1) > len(tag2):
		total = len(tag1)
	else:
		total = len(tag2)

	for index in range(total):
		# print index
		key = tag1[index].firstChild.nodeValue+';'+tag2[index].firstChild.nodeValue
		# print key
		if (key in counter):
			counter[key]  += 1
		else:
			counter[key] =  1

	print counter


	for key in counter.keys():
		print '%-31s  %s' %(key,str(counter[key]))
		copyToFile(key+'\t'+str(counter[key]))
	return
	
def copyToFile(text):
    try:
    	with open('counter.txt','a') as logFile:
    		logFile.write(text+'\n')
    except IOError as ie:
        print ie

	return


if __name__ == '__main__':
	file_name = sys.argv[1]
	print 'parsing the file "' + file_name + '"'
	main(file_name)