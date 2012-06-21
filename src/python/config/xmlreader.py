from xml.etree.ElementTree import ElementTree

tree = ElementTree()

def get_element(value):
    value = changeinputstring(value)
    tree.parse('..\\..\\..\\conf\\webarchive.conf.xml')
    if value == ( 'crawler/tinterval' or 'server/notify/interval'):
        return changeoutputstring(tree.findtext(value))
    else:
        return tree.findtext(value)

def changeinputstring(value):
    index = 0
    end = len(value)
    while index < end:
        if value[index] == "." :
            value = value[0:index] + '/' +  value[index+1:end]
        index=index + 1
    return value

def changeoutputstring(value):
    index = 0
    start = 0
    time = 0
    while index < len(value):
        if value[index] == ":" :
            time = time*60 + int(value[start:index])
            start = index + 1
        if index == len(value)-1:
            time = time*60 + int(value[start:index+1])
        index = index + 1
    time = str(time)
    return time

