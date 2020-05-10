import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PersonComponentsPage, PersonDeleteDialog, PersonUpdatePage } from './person.page-object';

const expect = chai.expect;

describe('Person e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let personComponentsPage: PersonComponentsPage;
  let personUpdatePage: PersonUpdatePage;
  let personDeleteDialog: PersonDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load People', async () => {
    await navBarPage.goToEntity('person');
    personComponentsPage = new PersonComponentsPage();
    await browser.wait(ec.visibilityOf(personComponentsPage.title), 5000);
    expect(await personComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.person.home.title');
    await browser.wait(ec.or(ec.visibilityOf(personComponentsPage.entities), ec.visibilityOf(personComponentsPage.noResult)), 1000);
  });

  it('should load create Person page', async () => {
    await personComponentsPage.clickOnCreateButton();
    personUpdatePage = new PersonUpdatePage();
    expect(await personUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.person.home.createOrEditLabel');
    await personUpdatePage.cancel();
  });

  it('should create and save People', async () => {
    const nbButtonsBeforeCreate = await personComponentsPage.countDeleteButtons();

    await personComponentsPage.clickOnCreateButton();

    await promise.all([
      personUpdatePage.setFirstNameInput('firstName'),
      personUpdatePage.setLastNameInput('lastName'),
      personUpdatePage.setPhoneNumberInput('phoneNumber'),
      personUpdatePage.setEmailAddressInput('emailAddress'),
      personUpdatePage.setSsnInput('ssn'),
      personUpdatePage.setBackgroundCheckDateInput('2000-12-31'),
      personUpdatePage.setBackgroundCheckConfirmationNumberInput('backgroundCheckConfirmationNumber'),
      personUpdatePage.setEmploymentVerificationDateInput('2000-12-31'),
      personUpdatePage.setEmploymentVerificationConfirmationNumberInput('employmentVerificationConfirmationNumber')
    ]);

    expect(await personUpdatePage.getFirstNameInput()).to.eq('firstName', 'Expected FirstName value to be equals to firstName');
    expect(await personUpdatePage.getLastNameInput()).to.eq('lastName', 'Expected LastName value to be equals to lastName');
    expect(await personUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await personUpdatePage.getEmailAddressInput()).to.eq('emailAddress', 'Expected EmailAddress value to be equals to emailAddress');
    const selectedPrimaryContact = personUpdatePage.getPrimaryContactInput();
    if (await selectedPrimaryContact.isSelected()) {
      await personUpdatePage.getPrimaryContactInput().click();
      expect(await personUpdatePage.getPrimaryContactInput().isSelected(), 'Expected primaryContact not to be selected').to.be.false;
    } else {
      await personUpdatePage.getPrimaryContactInput().click();
      expect(await personUpdatePage.getPrimaryContactInput().isSelected(), 'Expected primaryContact to be selected').to.be.true;
    }
    const selectedIsMinor = personUpdatePage.getIsMinorInput();
    if (await selectedIsMinor.isSelected()) {
      await personUpdatePage.getIsMinorInput().click();
      expect(await personUpdatePage.getIsMinorInput().isSelected(), 'Expected isMinor not to be selected').to.be.false;
    } else {
      await personUpdatePage.getIsMinorInput().click();
      expect(await personUpdatePage.getIsMinorInput().isSelected(), 'Expected isMinor to be selected').to.be.true;
    }
    expect(await personUpdatePage.getSsnInput()).to.eq('ssn', 'Expected Ssn value to be equals to ssn');
    expect(await personUpdatePage.getBackgroundCheckDateInput()).to.eq(
      '2000-12-31',
      'Expected backgroundCheckDate value to be equals to 2000-12-31'
    );
    expect(await personUpdatePage.getBackgroundCheckConfirmationNumberInput()).to.eq(
      'backgroundCheckConfirmationNumber',
      'Expected BackgroundCheckConfirmationNumber value to be equals to backgroundCheckConfirmationNumber'
    );
    expect(await personUpdatePage.getEmploymentVerificationDateInput()).to.eq(
      '2000-12-31',
      'Expected employmentVerificationDate value to be equals to 2000-12-31'
    );
    expect(await personUpdatePage.getEmploymentVerificationConfirmationNumberInput()).to.eq(
      'employmentVerificationConfirmationNumber',
      'Expected EmploymentVerificationConfirmationNumber value to be equals to employmentVerificationConfirmationNumber'
    );

    await personUpdatePage.save();
    expect(await personUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await personComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Person', async () => {
    const nbButtonsBeforeDelete = await personComponentsPage.countDeleteButtons();
    await personComponentsPage.clickOnLastDeleteButton();

    personDeleteDialog = new PersonDeleteDialog();
    expect(await personDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.person.delete.question');
    await personDeleteDialog.clickOnConfirmButton();

    expect(await personComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
