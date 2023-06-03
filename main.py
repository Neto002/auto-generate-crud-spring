import crud_spring_generator

print("It's important to notice that you must have Model and VO classes correctly implemented on your project so the script can work properly.")

crud_spring_generator.create_generated_files_directory()

package_name = str(input('Enter the package\'s base name: ')).strip()
endpoint_name = str(input('Enter the endpoint\'s name: ')).strip().lower()

class_types = ['controller', 'services', 'repository']

for class_type in class_types:
    crud_spring_generator.copy_template_file(f'./base_text_files/EndPoint{class_type.capitalize()}.txt', f'./files/{endpoint_name.capitalize()}{class_type.capitalize()}.java')

    crud_spring_generator.replace_template_content_in_file(package_name, endpoint_name, f'{endpoint_name.capitalize()}{class_type.capitalize()}.java', class_type=class_type) if (crud_spring_generator.are_variables_valid(package_name=package_name, endpoint_name=endpoint_name)) else print("Invalid package or endpoint names")

crud_spring_generator.create_dozer_mapper_class(package_name)