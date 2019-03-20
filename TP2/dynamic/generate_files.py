from numpy import random, pi, sin, cos
import sys

def generate_dynamic_file(number, length, name):
    f = open(name, 'w')
    f.write('{}\n'.format(length))
    for x in range(0, number):
        angle = random.uniform(-pi, pi)
        f.write('{x} {y} {angle}\n'.format(
                x = random.uniform(0, length),
                y = random.uniform(0,length),
                angle = angle,
            )
        )

def generate_files(index, number, length):
    generate_dynamic_file(number, length, str(index) + '-dynamic-' + str(number) + '.ari')

numbers = [50,100,200,300,400,500,600,700,800,900,1000,2000,3000]

if (len(sys.argv) == 0):
    length = 20
else:
    length = int(sys.argv[1])
i = 0
for x in numbers:
    i += 1
    generate_files(i, numbers[i-1], length)